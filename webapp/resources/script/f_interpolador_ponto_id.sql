-- Function: public.f_interpolador_ponto_id(integer, text, text, double precision, integer, double precision, text)

-- DROP FUNCTION public.f_interpolador_ponto_id(integer, text, text, double precision, integer, double precision, text);

CREATE OR REPLACE FUNCTION public.f_interpolador_ponto_id(
    p_interpolacao integer,
    p_refinterpolacao text,
    p_grade text,
    p_expoente double precision,
    p_elementos integer,
    p_raio double precision,
    p_coluna text)
  RETURNS double precision AS
$BODY$
DECLARE
    Cur_Amostras   	 refcursor;
    V_Pontos   		 GEOMETRY;
    V_Distancia 	 FLOAT;
    V_Medida    	 FLOAT;
    V_Soma      	 FLOAT:=0;
    V_Numerador		 FLOAT:=0;
    V_Denominador	 FLOAT:=0;
    V_Estimado		 FLOAT:=0;
    i 			 integer:=0;
    V_QUERY_SELECT 	 text;
BEGIN
  -- os parâmetros de entrada da função são ($1 -> código do ponto a ser interpolado, 
						--$2 -> nome da amostra, 
						--$3 -> nome da tabela que armazena os pixels (pixelmapa), 
						--$4 -> expoente da interpolacao, 
						--$5 -> numero de elementos, 
						--$6 -> raio (em metros), 
						--$7 -> nome da coluna de interpolação

						
	   V_QUERY_SELECT := 'SELECT  a.geometry, st_distance(st_centroid(b.geometry), a.geometry) as distancia,  a.'||P_Coluna||' as medida
           FROM '||P_RefInterpolacao||' a, '||P_Grade||'  b
           WHERE b.id = '||P_interpolacao;
           IF P_Elementos > 0 THEN
		V_QUERY_SELECT := V_QUERY_SELECT ||' order by distancia LIMIT '||P_Elementos ;
	   ELSE
	        V_QUERY_SELECT := V_QUERY_SELECT ||' and st_contains(st_buffer(st_centroid(b.geometry), '||P_Raio||'), a.geometry)';
           END IF;
           raise notice 'sql: %',v_query_select;

	OPEN Cur_Amostras FOR EXECUTE V_QUERY_SELECT;
	
	LOOP FETCH Cur_Amostras INTO V_Pontos, V_Distancia, V_Medida;
	  EXIT WHEN NOT FOUND;
           i:= i+1;
           V_Numerador := V_Numerador + (V_Medida/(V_Distancia^P_Expoente));
           V_Denominador := V_Denominador + (1/(V_Distancia^P_Expoente));
        END LOOP;

	IF i > 0 THEN
            V_Soma := V_Numerador/V_Denominador;
        ELSE
           RETURN 0;
        END IF;
      CLOSE Cur_Amostras;
      RETURN V_Soma;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.f_interpolador_ponto_id(integer, text, text, double precision, integer, double precision, text)
  OWNER TO postgres;
