-- Function: public.f_cria_mapa(text, double precision, text, integer, double precision, text, text, double precision, double precision, integer, integer, integer, integer, integer)

-- DROP FUNCTION public.f_cria_mapa(text, double precision, text, integer, double precision, text, text, double precision, double precision, integer, integer, integer, integer, integer);

CREATE OR REPLACE FUNCTION public.f_cria_mapa(
    p_descricaomapa text,
    p_expoente double precision,
    p_nometabelapixels text,
    p_npontos integer,
    p_tamanhoraio double precision,
    p_tipogeometria text,
    p_tipointerpolador text,
    p_tam_x double precision,
    p_tam_y double precision,
    p_amostracodigo integer,
    p_areacodigo integer,
    p_atributocodigo integer,
    p_usuariocodigo integer,
    p_classificadorcodigo integer)
  RETURNS boolean AS
$BODY$
declare
   v_sql   text;
   v_codmapa integer;
   v_resultgrade boolean;
begin

	IF ( p_classificadorcodigo IS NOT NULL AND p_classificadorcodigo != 0 ) THEN 

		v_sql := 'insert into sdum.tb_mapa (descricao, expoente, nomedatabela, numeropontos, tamanhoraio, tipogeometria, 
			 tipointerpolador, x, y, amostra_id, area_id, alcance, comp_principal, data, efeitopepita,
			 modelo, patamar, usuario_id, classificador_id) values ('''||p_descricaomapa||
			 ''', '||p_expoente||', '''||p_nometabelapixels||''', '||p_npontos||', '||p_tamanhoraio||', '''
			 ||p_tipogeometria||''', '''||p_tipointerpolador||''', '||p_tam_x||', '||p_tam_y||', '
			 ||p_amostracodigo||', '||p_areacodigo||', 0, ''NAO'', '''||current_date||''', 0, ''0'', 0, '||p_usuariocodigo||', '||p_classificadorcodigo||')';
	ELSE

		v_sql := 'insert into sdum.tb_mapa (descricao, expoente, nomedatabela, numeropontos, tamanhoraio, tipogeometria, 
			 tipointerpolador, x, y, amostra_id, area_id, alcance, comp_principal, data, efeitopepita,
			 modelo, patamar, usuario_id) values ('''||p_descricaomapa||
			 ''', '||p_expoente||', '''||p_nometabelapixels||''', '||p_npontos||', '||p_tamanhoraio||', '''
			 ||p_tipogeometria||''', '''||p_tipointerpolador||''', '||p_tam_x||', '||p_tam_y||', '
			 ||p_amostracodigo||', '||p_areacodigo||', 0, ''NAO'', '''||current_date||''', 0, ''0'', 0, '||p_usuariocodigo||')';
		
	END IF;


        
             
         execute v_sql;
         execute 'select id from sdum.tb_mapa where descricao = '''||p_descricaomapa||'''' into v_codmapa;
         raise notice 'insert: %', v_sql;
         V_SQL := 'select f_gera_grid_mapa('||p_areacodigo||', ''sdum.area'', '||p_tam_x||', '||p_tam_y||', '''||p_nometabelapixels||''', '||v_codmapa||', '''||p_tipogeometria||''', 4326)';
         raise notice 'insert: %', v_sql;
         execute v_sql into v_resultgrade;
         v_sql := 'select f_interpola_tabela(''sdum.tb_mapa'','''||p_descricaomapa||''', ''sdum.pixelamostra'','||p_expoente||
                                                    ','||p_npontos||','||p_tamanhoraio||',''valor'','''||p_tipointerpolador||''')';
         
	 execute v_sql;
     --    SELECT UpdateGeometrySRID('pixelamostra','geometry',4326);
  --      delete from pixelmapa;
  --      execute 'select f_gera_grid('||p_codarea||', '''||area
  return true;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.f_cria_mapa(text, double precision, text, integer, double precision, text, text, double precision, double precision, integer, integer, integer, integer, integer)
  OWNER TO postgres;
