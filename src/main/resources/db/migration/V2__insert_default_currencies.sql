-- Insertamos monedas base.
-- Usamos ON CONFLICT DO NOTHING por si re-ejecutamos scripts en entornos sucios.

INSERT INTO currencies (id, name, created_at)
VALUES ('MXN', 'Peso Mexicano', NOW())
    ON CONFLICT (id) DO NOTHING;

INSERT INTO currencies (id, name, created_at)
VALUES ('USD', 'Dólar Estadounidense', NOW())
    ON CONFLICT (id) DO NOTHING;