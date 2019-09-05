-- Function: public.f_buscadatum_utm(double precision, double precision)

-- DROP FUNCTION public.f_buscadatum_utm(double precision, double precision);

CREATE OR REPLACE FUNCTION public.f_buscadatum_utm(
    "long" double precision,
    lat double precision)
  RETURNS integer AS
$BODY$
DECLARE
    V_UTM_NORTE INTEGER[];
    V_UTM_SUL   INTEGER[];
    V_GOMO INTEGER;
BEGIN
    V_UTM_NORTE := ARRAY[32601, 32602, 32603, 32604, 32605, 32606, 32607, 32608, 32609, 32610, 32611, 32612, 32613, 
                         32614, 32615, 32616, 32617, 32618, 32619, 32620, 32621, 32622, 32623, 32624, 32625, 32626, 
                         32627, 32628, 32629, 32630, 32631, 32632, 32633, 32634, 32635, 32636, 32637, 32638, 32639, 
                         32640, 32641, 32642, 32643, 32644, 32645, 32646, 32647, 32648, 32649, 32650, 32651, 32652, 
                         32653, 32654, 32655, 32656, 32657, 32658, 32659, 32660];
                        
    V_UTM_SUL := ARRAY[32701, 32702, 32703, 32704, 32705, 32706, 32707, 32708, 32709, 32710, 32711, 32712, 32713, 
                       32714, 32715, 32716, 32717, 32718, 32719, 32720, 32721, 32722, 32723, 32724, 32725, 32726, 
                       32727, 32728, 32729, 32730, 32731, 32732, 32733, 32734, 32735, 32736, 32737, 32738, 32739, 
                       32740, 32741, 32742, 32743, 32744, 32745, 32746, 32747, 32748, 32749, 32750, 32751, 32752, 
                       32753, 32754, 32755, 32756, 32757, 32758, 32759, 32760];                 

    IF LONG >=0 THEN
      V_GOMO := 31 + TRUNC(ABS(LONG)/6);
    ELSE 
      V_GOMO := 31 - TRUNC(ABS(LONG)/6);
    END IF;
    RAISE NOTICE 'id:%', V_GOMO;

    IF LAT >= 0 THEN
       return V_UTM_NORTE[V_GOMO];
    ELSE 
       return V_UTM_SUL[V_GOMO];
    END IF;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.f_buscadatum_utm(double precision, double precision)
  OWNER TO postgres;
