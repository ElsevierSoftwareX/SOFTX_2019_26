
CREATE OR REPLACE FUNCTION public.f_gera_grid_mapa_interpolacao_r(
    p_codarea integer,
    p_nometabelaareas text,
    p_tam_x double precision,
    p_tam_y double precision,
    p_mapa text,
    p_codgrade integer,
    p_tipogeometria text,
    p_sriddefaultbanco integer)
  RETURNS boolean AS
$BODY$
DECLARE
  V_xmin  float;             -- x m�nimo do pol�gono
  V_xmax  float;             -- x m�ximo do poligono
  V_ymin  float;             -- y m�nimo do poligono
  V_ymax  float;             -- y m�ximo do poligono
  V_nPixel_x  integer;       -- n�mero de pixels de x
  V_nPixel_y  integer;       -- n�mero de pixels de y  
  V_largura   float;         -- largura total da �rea;
  V_altura    float;         -- altura total da �rea;
  V_tamPixel_x float;        -- tamanho do pixel x
  V_tamPixel_y float;        -- tamanho do pixel y
  V_x          integer := 0; -- usado para o LOOP
  V_y          integer := 0; -- usado para o LOOP
  Posicao_x    float;        -- necess�rio para quando o poligono inicia na posi��o (0,0)
  Posicao_y    float;        -- necess�rio para quando o poligono inicia na posi��o (0,0)
  V_Str_Insert text;         -- string para executar o insert
  V_str_Delete text;         -- string para executar o delete dos pontos que est�o fora do poligono
  V_str_DeleteST_POINT text; -- string para executar o delete dos pontos inseridos no banco na forma de ST_POINT, pois o R devolve ponto e n�o pol�gono
  local_srid    integer;   -- ALTERAR QUANDO FOR PRECISO ALTERAR A REGI�O
  v_longcentral float;
  v_latcentral float;
BEGIN
      execute 'select st_x(st_centroid(the_geom)), st_y(st_centroid(the_geom)) from '||p_nometabelaareas|| '
               where id = '||p_codarea into v_longcentral, v_latcentral;
      select f_buscadatum_utm(v_longcentral, v_latcentral) into local_srid;
      raise notice 'srid:%', local_srid;
	  
      EXECUTE 'Select st_xmin(st_transform(the_geom,'||local_srid||')), st_xmax(st_transform(the_geom,'||local_srid||')),  st_ymin(st_transform(the_geom,'||local_srid||')), st_ymax(st_transform(the_geom,'||local_srid||')) from '||P_NomeTabelaAreas||' where id = '||P_Codarea INTO V_xmin, V_xmax, V_ymin, V_ymax;
      V_largura    := V_xmax - V_xmin;            -- determina a largura m�xima da �rea
      V_altura     := V_ymax - V_ymin;            -- determina a altura m�xima da �rea
       raise notice 'XMIN: % ', V_xmin;
       raise notice 'XMAX: % ', V_xmax;
       raise notice 'YMIN: % ', V_ymin;
       raise notice 'YMAX: % ', V_ymax;
                  
       raise notice 'largura: % - altura: %', v_largura, v_altura;
      V_nPixel_x   := round(V_largura/P_tam_x);   -- define a quantidade de pontos na horizontal
       raise notice 'npixelsX: %',v_npixel_x;
      V_nPixel_y   := round(V_altura/P_tam_y);   -- define a quantidade de pontos na vertical
       raise notice 'npixelsY: %',v_npixel_y;
     -- V_tamPixel_x := V_largura / V_nPixel_x;     -- define o tamanho de cada ponto da horizontal
     -- V_tamPixel_y := V_altura / V_nPixel_y;      -- define o tamanho de cada ponto da vertical
      V_tamPixel_x := p_tam_x;     -- define o tamanho de cada ponto da horizontal
      V_tamPixel_y := p_tam_y;      -- define o tamanho de cada ponto da vertical


    --  EXECUTE 'CREATE TABLE '||P_Mapa||' (	gid serial primary key, 
	--					gri_medida float)';   -- cria a tabela onde o novo gride ser� armazenado
    -- EXECUTE ' SELECT AddGeometryColumn('''||P_Mapa||''', ''geometry'', '||P_SridDefaultBanco||', '''||P_Tipogeometria||''', 2)'; 

      FOR V_y IN 0..V_nPixel_y LOOP
          FOR V_x IN 0..V_nPixel_x LOOP
              Posicao_x := V_xmin + (V_x * V_tamPixel_x)+(V_tamPixel_x/2);  -- define a posi��o do X para a geometria
               raise notice 'posi��o x: %', round(cast (Posicao_x as numeric),6);
              Posicao_y := (V_ymin + (V_y * V_tamPixel_y)+(V_tamPixel_y/2)); -- define a posi��o do Y para a geometria
               raise notice 'posi��o y: %', round(cast(Posicao_y as numeric),6);

			  IF P_Tipogeometria = 'POINT' THEN  
					raise notice 'point--------';
					  V_str_Insert := 'insert into '||P_Mapa||'(the_geom, pix_mapcodigo) values '||
									  '(st_transform(st_geomfromtext(''POINT('||round(cast (Posicao_x as numeric),4)|| ' '||round(cast(Posicao_y as numeric),4)||')'', '||local_Srid||'), '||P_SridDefaultBanco||'), '||p_codgrade||');';   
			  ELSE
					  raise notice 'not point--------';
					  V_str_Insert := 'insert into '||P_Mapa||'(the_geom, pix_mapcodigo) values '||
									  '(st_transform(st_geomfromtext(''POLYGON(('||Posicao_x-(V_TamPixel_x/2)|| ' '||Posicao_y-(V_TamPixel_y/2)||',
									   '||Posicao_x+(V_TamPixel_x/2)|| ' '||Posicao_y-(V_TamPixel_y/2)||',
									   '||Posicao_x+(V_TamPixel_x/2)|| ' '||Posicao_y+(V_TamPixel_y/2)||',
									   '||Posicao_x-(V_TamPixel_x/2)|| ' '||Posicao_y+(V_TamPixel_y/2)||',
									   '||Posicao_x-(V_TamPixel_x/2)|| ' '||Posicao_y-(V_TamPixel_y/2)||'))'', '||local_srid||'),'||P_SridDefaultBanco||'), '||p_codgrade||');';   
			  END IF;
					  execute V_Str_Insert;          
		  END LOOP;
      END LOOP;
	  
	  V_str_Delete := 'delete from '||P_Mapa||' b where b.pix_mapcodigo = '|| p_codgrade ||' and b.pix_codigo not in (select a.pix_codigo from '||P_Mapa||' a, '||p_nometabelaareas||' c
            where (st_Intersects( c.the_geom, a.the_geom) or st_contains( c.the_geom, a.the_geom) ) and c.id = '||P_Codarea||')';
      execute V_str_Delete;

      execute 'select f_gera_grid_mapa_interpolacao_r2('||p_codgrade||')';
	  
      return true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;