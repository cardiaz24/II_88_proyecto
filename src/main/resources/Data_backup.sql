-- Categorías
INSERT INTO categoria (id, nombre) VALUES (1, 'Ficción');
INSERT INTO categoria (id, nombre) VALUES (2, 'Ciencia');

-- Autores
INSERT INTO autor (id, nombre, pais) VALUES (1, 'Gabriel García Márquez', 'Colombia');
INSERT INTO autor (id, nombre, pais) VALUES (2, 'Isaac Asimov', 'Rusia');

-- Libros
INSERT INTO libro (id, titulo, unidades, categoria_id) VALUES (1, 'Cien años de soledad', 5, 1);
INSERT INTO libro (id, titulo, unidades, categoria_id) VALUES (2, 'Fundación', 7, 2);

-- Relación N:M libro_autores
INSERT INTO libro_autores (libro_id, autores_id) VALUES (1, 1);
INSERT INTO libro_autores (libro_id, autores_id) VALUES (2, 2);