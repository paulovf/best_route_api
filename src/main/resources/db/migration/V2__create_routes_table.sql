CREATE TABLE routes (
    id UUID NOT NULL DEFAULT generate_uuid_v7(),
    origin_city TEXT NOT NULL,
    origin_state CHAR(2) NOT NULL,
    destination_city TEXT NOT NULL,
    destination_state CHAR(2) NOT NULL,
    travel_date TIMESTAMP WITH TIME ZONE NOT NULL,
    api_response jsonb,
    inserted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (id, travel_date)
) PARTITION BY RANGE (travel_date);

CREATE UNIQUE INDEX uk_route_search_cache_insensitive
    ON routes (
       lower(public.immutable_unaccent(origin_city)),
       lower(origin_state),
       lower(public.immutable_unaccent(destination_city)),
       lower(destination_state),
       travel_date
    );

CREATE TABLE routes_default PARTITION OF routes DEFAULT;