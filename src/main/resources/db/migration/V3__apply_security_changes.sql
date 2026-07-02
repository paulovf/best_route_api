CREATE SCHEMA IF NOT EXISTS extensions;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'unaccent') THEN
        ALTER EXTENSION unaccent SET SCHEMA extensions;
ELSE
        CREATE EXTENSION unaccent SCHEMA extensions;
END IF;
END $$;

CREATE OR REPLACE FUNCTION public.immutable_unaccent(text)
RETURNS text AS $$
SELECT extensions.unaccent($1);
$$ LANGUAGE sql IMMUTABLE PARALLEL SAFE
SET search_path = pg_catalog, extensions;

CREATE OR REPLACE FUNCTION public.generate_uuid_v7()
RETURNS uuid AS $$
DECLARE
v_time_ms bigint;
    v_time_hex text;
    v_bytes bytea;
BEGIN
    v_time_ms := floor(extract(epoch from clock_timestamp()) * 1000)::bigint;
    v_time_hex := lpad(to_hex(v_time_ms), 12, '0');

    v_bytes := gen_random_bytes(10);

RETURN encode(
        set_byte(
                set_byte(
                        decode(v_time_hex || encode(v_bytes, 'hex'), 'hex'),
                        6, (get_byte(decode(v_time_hex || encode(v_bytes, 'hex'), 'hex'), 6) & 15) | 112
                ),
                8, (get_byte(decode(v_time_hex || encode(v_bytes, 'hex'), 'hex'), 8) & 63) | 128
        ),
        'hex'
       )::uuid;
END;
$$ LANGUAGE plpgsql VOLATILE
SET search_path = pg_catalog;