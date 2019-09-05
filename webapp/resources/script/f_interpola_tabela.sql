-- Function: public.f_interpola_tabela(text, text, text, double precision, integer, double precision, text, text)

-- DROP FUNCTION public.f_interpola_tabela(text, text, text, double precision, integer, double precision, text, text);

CREATE OR REPLACE FUNCTION public.f_interpola_tabela(
    p_tabelagrade text,
    p_descricaograde text,
    p_tabelaamostra text,
    expoente double precision,
    p_npontos integer,
    p_raio double precision,
    p_coluna text,
    p_tipointerpolador text)
  RETURNS boolean AS
$BODY$
DECLARE
     cursor_grade    refcursor;
     record_cursor   record;
     valor_interpolado float;
     v_codgrade integer;
     teste             boolean;
     testando		text;
     teste1	text;
BEGIN
 --    EXECUTE 'CREATE TABLE '||P_TabelaGrade||' as select * from tb_ref';
 --    EXECUTE 'SELECT F_ADD_GEOMETRY_COLUMNS(''public'',''the_geom'', '''||P_TabelaGrade||''', ''POINT'', 3, -1)' into teste;

    raise notice '------**************************-%--------------%------', p_tabelagrade, p_descricaograde;
      execute 'select id from '||p_tabelagrade||' where descricao = '''||p_descricaograde||'''' into v_codgrade; 
     OPEN Cursor_grade FOR EXECUTE 'SELECT * FROM sdum.pixelmapa where mapa_id = '||v_codgrade;
         raise notice '------**************************---------------------';

     LOOP FETCH Cursor_grade INTO Record_cursor;
        EXIT WHEN NOT FOUND;
         IF P_TipoInterpolador = 'MM' THEN
              EXECUTE 'select F_INTERPOLADOR_PONTO_MM('||record_cursor.id||', '''|| P_TabelaAmostra||''', ''sdum.pixelmapa'', cast('||expoente||' as double precision), '||P_NPontos||', cast('||P_Raio||' as double precision),'''||P_Coluna||''')' into valor_interpolado;
	 ELSE
              EXECUTE 'select F_INTERPOLADOR_PONTO_ID('||record_cursor.id||', '''|| P_TabelaAmostra||''', ''sdum.pixelmapa'', cast('||expoente||' as double precision), '||P_NPontos||', cast('||P_Raio||' as double precision),'''||P_Coluna||''')' into valor_interpolado;
	 END IF;
	EXECUTE 'UPDATE sdum.pixelmapa set valor = '||valor_interpolado||' where id = '||record_cursor.id;
     END LOOP;
return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.f_interpola_tabela(text, text, text, double precision, integer, double precision, text, text)
  OWNER TO postgres;
