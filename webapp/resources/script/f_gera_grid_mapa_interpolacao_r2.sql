--select f_gera_grid_mapa_interpolacao_r2(45)
--select f_gera_grid_mapa_interpolacao_r(20, 'sdum.tb_area',15, 15, 'sdum.tb_pixelmapa', 45, 'POLYGON', 4326)



CREATE OR REPLACE FUNCTION public.f_gera_grid_mapa_interpolacao_r2(
    p_codgrade integer)
  RETURNS boolean AS
$BODY$
DECLARE
  v_cursor refcursor;
  v_cursoraux refcursor;
  v_dado record;
  v_update record;
  v_sql text;
  i integer :=1;
  aux integer :=0;
  vetor_pixcodigo  integer[];
  quantidadepontos  integer :=0;
BEGIN

	execute 'select * from sdum.tb_pixelmapa WHERE  ST_GeometryType(the_geom) = ''ST_Point'' and pix_mapcodigo= '||p_codgrade into quantidadepontos;
	open v_cursor for execute 'select * from sdum.tb_pixelmapa WHERE  ST_GeometryType(the_geom) = ''ST_Point'' and pix_mapcodigo= '||p_codgrade;
	open v_cursoraux for execute 'select * from sdum.tb_pixelmapa WHERE  ST_GeometryType(the_geom) = ''ST_Point'' and pix_mapcodigo= '||p_codgrade;

	LOOP FETCH v_cursor INTO v_dado;
	EXIT WHEN NOT FOUND;
		--raise notice 'v_dado.pix_codigo: %',v_dado.pix_codigo;
		execute 'select b.pix_codigo from sdum.tb_pixelmapa b,  sdum.tb_pixelmapa a WHERE  ST_GeometryType(a.the_geom) = ''ST_Point'' and a.pix_mapcodigo='||p_codgrade||' and 
			b.pix_mapcodigo= '||p_codgrade||' and  ST_GeometryType(b.the_geom) = ''ST_Polygon'' and st_contains(b.the_geom, a.the_geom) and a.pix_codigo= '||v_dado.pix_codigo into aux;
		vetor_pixcodigo[i] := aux;
		--raise notice 'AUX ---: %', aux;
		i := i+1;
	END LOOP;
	Close v_cursor;
	
	i:=1;
	LOOP FETCH v_cursoraux INTO v_update;
	EXIT WHEN NOT FOUND;
		raise notice 'vetor_pixcodigo[i]: %', vetor_pixcodigo[i];
		v_sql := 'UPDATE sdum.tb_pixelmapa SET the_geom = (select b.the_geom from sdum.tb_pixelmapa b WHERE b.pix_codigo= '||vetor_pixcodigo[i]||') where pix_codigo = '||v_update.pix_codigo;
		execute v_sql;
		i := i+1;
	END LOOP;
	Close v_cursoraux;

	execute 'delete from sdum.tb_pixelmapa where (pix_mapcodigo = '||p_codgrade||' and pix_valor is NULL)';
      
      return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;