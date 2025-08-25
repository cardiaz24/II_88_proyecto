-- Insertar categorías
INSERT INTO categoria (id, nombre) VALUES 
(1, 'Novela'),
(2, 'Tecnología'),
(3, 'Historia'),
(4, 'Filosofía'),
(5, 'Ciencia');

-- Insertar autores
INSERT INTO autor (id, nombre, pais) VALUES 
(1, 'Gabriel García Márquez', 'Colombia'),
(2, 'Martin Fowler', 'Reino Unido'),
(3, 'Yuval Noah Harari', 'Israel'),
(4, 'Jostein Gaarder', 'Noruega'),
(5, 'Stephen Hawking', 'Reino Unido');

-- Insertar libros
INSERT INTO libro (id, titulo, isbn, anio_publicacion, descripcion, unidades, categoria_id) VALUES 
(1, 'Cien años de soledad', '978-8437604947', 1967, 'Una novela del realismo mágico', 5, 1),
(2, 'Refactoring', '978-0201485677', 1999, 'Mejorando el diseño de código existente', 3, 2),
(3, 'Sapiens', '978-0062316097', 2011, 'De animales a dioses', 4, 3),
(4, 'El mundo de Sofía', '978-8478448152', 1991, 'Novela sobre la historia de la filosofía', 2, 4),
(5, 'Breves respuestas a grandes preguntas', '978-6075274801', 2018, 'Reflexiones sobre el universo', 3, 5);

-- Relacionar libros con autores
INSERT INTO libro_autor (libro_id, autor_id) VALUES 
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Insertar ejemplares
INSERT INTO ejemplar (id, codigo, libro_id, prestado) VALUES 
(1, 'LIB-001-1', 1, false),
(2, 'LIB-001-2', 1, false),
(3, 'LIB-002-1', 2, false),
(4, 'LIB-003-1', 3, false),
(5, 'LIB-004-1', 4, false),
(6, 'LIB-005-1', 5, false),
(7, 'LIB-005-2', 5, false);

-- Insertar usuario administrador (password: admin123)
INSERT INTO usuario (id, nombre, apellidos, username, password, email, rol, enabled, multa_pendiente) VALUES 
(1, 'Administrador', 'Del Sistema', 'admin', 
'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubhFi', 
'admin@biblioteca.com', 'ADMIN', true, 0);

-- Insertar usuario normal (password: user123)
INSERT INTO usuario (id, nombre, apellidos, username, password, email, rol, enabled, multa_pendiente) VALUES 
(2, 'Usuario', 'Normal', 'user', 
'$2a$10$ZM8yLk2sV/5sTVsCQ82B.eJtvjpI9r6N5QeS7pJ4q4z4q4q4q4q4q', 
'user@biblioteca.com', 'USER', true, 0);

-- Insertar préstamos de ejemplo
INSERT INTO prestamo (id, usuario_id, ejemplar_id, fecha_prestamo, fecha_devolucion, fecha_vence, multa, estado) VALUES 
(1, 2, 1, '2025-08-10', NULL, '2025-08-24', 0, 'ACTIVO'),
(2, 2, 3, '2025-07-20', '2025-08-10', '2025-08-03', 2100, 'CON_MORA'),
(3, 1, 4, '2025-07-01', '2025-07-10', '2025-07-15', 0, 'DEVUELTO');