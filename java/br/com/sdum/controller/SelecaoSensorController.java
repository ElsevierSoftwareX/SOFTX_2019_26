package br.com.sdum.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.dao.GradeAmostralDAO;
import br.com.sdum.dao.PixelAmostraDAO;
import br.com.sdum.dao.PontoAmostralDAO;
import br.com.sdum.dao.PontoSensorlDAO;
import br.com.sdum.dao.SelecaoSensorDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.GradeAmostral;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.PontoAmostral;
import br.com.sdum.model.PontoSensor;
import br.com.sdum.model.SelecaoSensor;
import br.com.sdum.session.UsuarioSession;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Resource
@Path("selecaosensor")
public class SelecaoSensorController implements Serializable {

    private static final long serialVersionUID = 1846035799563404394L;

    private static int projection = 4326;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Inject
    private AmostraDAO amostraDAO;

    @Inject
    private SelecaoSensorDAO selecaoSensorDAO;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private GradeAmostralDAO gradeAmostralDAO;

    @Inject
    private PontoAmostralDAO pontoAmostralDAO;

    @Inject
    private PontoSensorlDAO pontoSensorDAO;

    @Path("lista")
    @Permissao(Roles.ON)
    public void lista(Long id) {

        try {
            Area area = areaDAO.buscarPorId(id);
            Long idArea = area.getId();
            SelecaoSensor selecaoSensor = selecaoSensorDAO.buscarPorId(idArea);
            //Area area = areaDAO.buscarPorId(id);

            //alterando para consertar seleção sensor
            //fim alteração
            // GradeAmostral gradeAmostral = new GradeAmostral();
            result.include("idArea", id).include("area.geom", area.getGeometry()).include(areaDAO.buscarPorId(idArea)).include("idProjeto", area.getProjeto().getId()).include("listaSelecaoSensor",
                    selecaoSensorDAO.listaSelecaoSensorByArea(idArea));//.include("pontosSensores", selecaoSensor.getPontoSensor()).include("pontoAmostral", selecaoSensor.getAmostras());
            // .include("gradeAmostral", gradeAmostral);
        } catch (Exception e) {
            System.out.println("Exception lista selecaosensor: " + e);
            result.include("idProjeto", areaDAO.buscarPorId(id).getProjeto().getId()).redirectTo(MapaController.class).mapa(areaDAO.buscarPorId(id).getProjeto().getId());
        }
    }

    @Path("exportartxt")
    @Permissao(Roles.ON)
    public void exportartxt(Long id, HttpServletResponse response) {
        SelecaoSensor selecaoSensor = selecaoSensorDAO.buscarPorId(id);
        try {
            String send = "Long\tLat" + System.getProperty("line.separator");
            List<PontoSensor> pontosensor = selecaoSensor.getPontoSensor();
            for (int i = 0; i < pontosensor.size(); i++) {
                String coordenadaspixel = pontosensor.get(i).getPontoAmostral().getThe_geom().toString();
                coordenadaspixel = coordenadaspixel.replace("POINT (", "");
                coordenadaspixel = coordenadaspixel.replace(")", "");
                //System.out.println("pixel.tostring() " + coordenadaspixel);
                String arraypixel[] = coordenadaspixel.split(" ");
                send += arraypixel[0] + "\t" + arraypixel[1] + "\t" + System.getProperty("line.separator");
            }
            byte[] report = send.getBytes();
            response.setContentType("application/save");
            response.setContentLength(report.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + selecaoSensor.getDescricao() + ".txt\"");
            response.addHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(report, 0, report.length);
                outputStream.flush();
            }
            result.include("idProjeto", selecaoSensor.getArea().getProjeto().getId()).redirectTo(this).lista(selecaoSensor.getArea().getId());
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        }
    }

    @Path("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long id) {

        Area area = areaDAO.buscarPorId(id);

        GeometryJSON g = new GeometryJSON();
        area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                + g.toString(area.getGeometry()) + "}");

        result.include("area", area).include("idProjeto", area.getProjeto().getId())
                // .include("idProjeto", area.getProjeto().getId())
                .include("listaAmostra", amostraDAO.listaAmostraByArea(id));
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void poligonoProjetoArea(Long idArea, SelecaoSensor selecaoSensor, String pontosLong, String pontosLat, int qtdepontos,
            GradeAmostral gradeAmostral, String descricaoGrade, String[] camposMarcados, int numeroSensores, String descricaoSensor,
            boolean formGrade) {
        try {
            Long idGrade;
            int diferenca;
            int fatsensor = 1;
            int fatpontos = 1;
            int fatdiferenca = 1;
            int cont = 1;
            Integer combinacoes = 1;

            if (formGrade) {

                String[] pontosGradeLong = pontosLong.split(",");
                String[] pontosGradeLat = pontosLat.split(",");

                System.out.println("-----LAT " + pontosLat);
                System.out.println("-----LONG " + pontosLong);

                gradeAmostral.setDataCadastro(new Date());
                gradeAmostral.setUsuario(usuarioSession.getUsuario());
                gradeAmostral.setArea(areaDAO.buscarPorId(idArea));
                gradeAmostral.setDescricao(descricaoGrade);
                gradeAmostral.setFlagsensor(true);
                gradeAmostralDAO.adicionar(gradeAmostral);

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                WKTReader reader = new WKTReader(geometryFactory);

                List<PontoAmostral> pontoAmostral = new ArrayList<PontoAmostral>();

                try {

                    String pontox;
                    String pontoy;

                    for (int i = 0; i < pontosGradeLat.length; i++) {
                        pontox = pontosGradeLong[i];
                        System.out.println("X: " + pontox);
                        pontoy = pontosGradeLat[i];
                        System.out.println("Y: " + pontoy);
                        PontoAmostral pa = new PontoAmostral();
                        pa.setGradeAmostral(gradeAmostral);
                        pa.setThe_geom(reader.read("POINT(" + pontox + " " + pontoy + ")"));
                        pa.getThe_geom().setSRID(projection);
                        pontoAmostral.add(pa);

                    }

                    qtdepontos = pontosGradeLat.length;
                    System.out.println("Quantidade pontos grade manual: " + qtdepontos);

                } catch (Exception e) {
                    System.out.println("Caiu no CATCH!" + e);
                }

                gradeAmostral.setPontoAmostral(pontoAmostral);
                //gradeAmostral.setFlagsensor(true);
                gradeAmostralDAO.alterar(gradeAmostral);
                idGrade = gradeAmostral.getId();

            } else {
                System.out.println("Entrou no else, ou seja, formGrade = false! ------- " + formGrade);
                gradeAmostral.setDataCadastro(new Date());
                gradeAmostral.setUsuario(usuarioSession.getUsuario());
                gradeAmostral.setDescricao(descricaoGrade);
                gradeAmostral.setArea(areaDAO.buscarPorId(idArea));
                gradeAmostral.setFlagsensor(true);

                gradeAmostralDAO.adicionar(gradeAmostral);
                Area areaAux = areaDAO.buscarPorId(idArea);
                gradeAmostralDAO.gerarGradeAmostral(areaAux.getId(), gradeAmostral.getTamx(), gradeAmostral.getTamy(),
                        gradeAmostral.getId());
                idGrade = gradeAmostral.getId();
                List<PontoAmostral> pontosAmostrais = pontoAmostralDAO.buscaPorIdPontoAmostral(idGrade);
                //if (pontosAmostrais.size() > 7) {
                //    qtdepontos = 7;
                //}else{
                qtdepontos = pontosAmostrais.size();
                System.out.println("Quantidade pontos grade automática: " + qtdepontos);
                // }

            }
            Integer qtdepontosfat = qtdepontos;
            Integer numeroSensoresfat = numeroSensores;
            if (qtdepontos > 12) {
                qtdepontosfat = 12;
            }
            if (numeroSensores > 12) {
                numeroSensoresfat = 11;
            }
            if (numeroSensoresfat > qtdepontosfat) {
                numeroSensoresfat = qtdepontosfat;
            }
            if (numeroSensores > qtdepontos) {
                numeroSensores = qtdepontos;
            }
            diferenca = Math.abs(qtdepontosfat - numeroSensoresfat);
            if (diferenca == 0) {
                diferenca = 1;
            }
            do {
                for (int i = 1; i <= numeroSensoresfat; i++) {
                    fatsensor = fatsensor * i;
                }
                System.out.println("fatsensor: " + fatsensor);
                cont++;
            } while (cont < 2);
            cont = 1;
            do {
                for (int i = 1; i <= qtdepontosfat; i++) {
                    fatpontos = fatpontos * i;
                }
                System.out.println("fatpontos: " + fatpontos);
                cont++;
            } while (cont < 2);
            cont = 1;
            do {
                for (int i = 1; i <= diferenca; i++) {
                    fatdiferenca = fatdiferenca * i;
                }
                System.out.println("fatdiferenca: " + fatdiferenca);
                cont++;
            } while (cont < 2);

            combinacoes = fatpontos / (fatsensor * fatdiferenca);
            if(combinacoes > 1000){
                combinacoes = 1000;
            }
            System.out.println("_----_ COMBINACOES: " + combinacoes);

            Long idAmostra = null;
            String nomesAmostrasInterpoladas = "";
            String nomeCampoMedida = "";
            List<Amostra> amostras = new ArrayList<Amostra>();
            String descricaoAmostraposicaofinal = "";

            System.out.println("Area: " + areaDAO.buscarPorId(idArea));
            System.out.println("area id: " + idArea);
            System.out.println("Campos marcados: " + camposMarcados.toString());
            System.out.println("Número de sensores: " + numeroSensores);
            System.out.println("Descricao Sensor: " + descricaoSensor);
            System.out.println("FormGrade : " + formGrade);

            for (int i = 0; i < camposMarcados.length; i++) {
                idAmostra = Long.parseLong(camposMarcados[i]);
                Amostra amostra = new Amostra();
                amostra = amostraDAO.buscarPorId(idAmostra);
                amostras.add(amostra);
                String eliminaespaconome = amostra.getDescricao().replaceAll(" ", "_");
                selecaoSensorDAO.gerarInterpolacao(idAmostra, idGrade);
                if (i == camposMarcados.length - 1) {
                    nomesAmostrasInterpoladas += "'sdum.tb_" + eliminaespaconome + "_sensor'";
                    nomeCampoMedida += "'pix_valor'";
                    descricaoAmostraposicaofinal = eliminaespaconome + "_sensor";
                } else {
                    nomesAmostrasInterpoladas += "'sdum.tb_" + eliminaespaconome + "_sensor', ";
                    nomeCampoMedida += "'pix_valor', ";
                }
            }

            selecaoSensor.setAmostras(amostras);
            selecaoSensor.setDescricao(descricaoSensor);
            selecaoSensor.setArea(areaDAO.buscarPorId(idArea));
            selecaoSensorDAO.adicionar(selecaoSensor);

            for (int i = 0; i < camposMarcados.length; i++) {
                idAmostra = Long.parseLong(camposMarcados[i]);
                List<SelecaoSensor> selecaosensores = new ArrayList<SelecaoSensor>();
                Amostra amostra = new Amostra();
                amostra = amostraDAO.buscarPorId(idAmostra);
                selecaosensores.addAll(amostra.getSelecoesSensores());
                selecaosensores.add(selecaoSensor);
                amostra.setSelecoesSensores(selecaosensores);
                amostraDAO.alterarT(amostra);
            }

            String retorno1 = selecaoSensorDAO.inserirOpcoesSensor(numeroSensores, nomesAmostrasInterpoladas, combinacoes, qtdepontos);
            System.out.println("------------- Retorno f_inserecombinacoes: " + retorno1);
            String retorno = selecaoSensorDAO.selecaoSensorFuzzy(nomesAmostrasInterpoladas, nomeCampoMedida, numeroSensores);
            String[] retornoArray = retorno.split(",");
            selecaoSensor.setFpi(Float.parseFloat(retornoArray[0]));
            selecaoSensor.setMpe(Float.parseFloat(retornoArray[1]));
            selecaoSensorDAO.alterarT(selecaoSensor);
            retornoArray[2] = retornoArray[2].replace("(", "");
            retornoArray[retornoArray.length - 1] = retornoArray[retornoArray.length - 1].replace(")", "");
            int aux = 2;
            String idSensor = "";
            do {
                System.out.println("---aux: " + aux);
                idSensor = retornoArray[aux];
                System.out.println("---idSensor: " + idSensor);
                PontoSensor pontoSensor = new PontoSensor();
                pontoSensor.setSelecaosensor(selecaoSensor);
                pontoSensor.setPontoAmostral(pontoAmostralDAO.buscaPorIdPontoAmostralSensor(idSensor, descricaoAmostraposicaofinal));
                pontoSensorDAO.adicionar(pontoSensor);
                aux += 1;
                System.out.println("-----------aux " + aux);
            } while (aux < retornoArray.length);
            //nomesAmostrasInterpoladas = nomesAmostrasInterpoladas.replace("'", "");
            //System.out.println("nomesAmostrasInterpoladas: " + nomesAmostrasInterpoladas);
            //String[] tabelas = nomesAmostrasInterpoladas.split(",");
            selecaoSensorDAO.excluiTabela(nomesAmostrasInterpoladas);
            result.include("success", "success.sensor.cadastrado").include("idProjeto", selecaoSensor.getArea().getProjeto().getId()).redirectTo(this).lista(selecaoSensor.getArea().getId());
        } catch (Exception e) {
            System.out.println("Exception persiste selecaosensor: " + e);
            result.include("idProjeto", areaDAO.buscarPorId(idArea).getProjeto().getId()).redirectTo(this).lista(idArea);
        }
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long id) {
        Amostra amostra;
        SelecaoSensor selecaoSensor = selecaoSensorDAO.buscarPorId(id);
        List<Amostra> amostras = selecaoSensor.getAmostras();
        int tamArray = amostras.size();
        try {
            for (int i = 0; i < tamArray; i++) {
                amostra = amostraDAO.buscarPorId(amostras.get(0).getId());
                amostra.getSelecoesSensores().remove(selecaoSensor);
                selecaoSensor.getAmostras().remove(amostra);
                amostraDAO.alterarT(amostra);

            }
            selecaoSensorDAO.alterarT(selecaoSensor);
            //	selecaoSensor.getAmostras().remove(amostras);

            selecaoSensorDAO.excluir(selecaoSensor);
            result.include("success", "success.excluir.selecaosensor").include("idProjeto", selecaoSensor.getArea().getProjeto().getId()).redirectTo(this)
                    .lista(selecaoSensor.getArea().getId());
        } catch (Exception e) {
            System.out.println("Exception excluir selecaosensor: " + e);
            result.include("danger", "danger.excluir.selecaosensor").include("idProjeto", selecaoSensorDAO.buscarPorId(id).getArea().getProjeto().getId()).redirectTo(this)
                    .lista(selecaoSensor.getArea().getId());

        }
    }

    @Path("visualizar")
    @Permissao(Roles.ON)
    public void visualizar(Long id) {
        try {
            SelecaoSensor selecaoSensor = selecaoSensorDAO.buscarPorId(id);
            List<Amostra> amostras = new ArrayList<Amostra>();
            amostras = selecaoSensor.getAmostras();
            Amostra amostra = new Amostra();
            amostra = amostras.get(0);
            List<PixelAmostra> pixelAmostra = amostra.getPixelAmostra();
            Area area = areaDAO.buscarPorId(selecaoSensor.getArea().getId());
            GeometryJSON gArea = new GeometryJSON();
            area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                    + gArea.toString(area.getGeometry()) + "}");
            selecaoSensor.setArea(area);

            GeometryJSON g = new GeometryJSON();

            List<String> foo = new ArrayList<String>();
            for (int i = 0; i < selecaoSensor.getPontoSensor().size(); i++) {
                foo.add("{"
                        + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + ","
                        + '"' + "id" + '"' + ":" + '"' + selecaoSensor.getPontoSensor().get(i).getId() + '"' + ","
                        + '"' + "geometry" + '"' + ":" + g.toString(selecaoSensor.getPontoSensor().get(i).getPontoAmostral().getThe_geom()) + "}");
            }

            String jsonPontoSensor = new Gson().toJson(foo);

            result.include("selecaoSensor", selecaoSensor)
                    .include("pontosSensores", jsonPontoSensor)
                    .include("idProjeto", selecaoSensor.getArea().getProjeto().getId())
                    .include("amostras", amostras);
            System.out.println("-------------- selecaosensor: " + jsonPontoSensor);
            //result.redirectTo(this).lista(selecaoSensor.getArea().getId());
        } catch (Exception e) {
            System.out.println("Exception visualizar selecaosensor: " + e);
            result.include("idProjeto", selecaoSensorDAO.buscarPorId(id).getArea().getProjeto().getId()).redirectTo(this).lista(selecaoSensorDAO.buscarPorId(id).getArea().getId());
        }
    }

}
