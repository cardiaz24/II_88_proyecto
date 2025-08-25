-- Crear secuencia para IDs
IF NOT EXISTS (SELECT * FROM sys.sequences WHERE name = 'hibernate_sequence')
CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1;

-- Tabla de usuarios
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR hibernate_sequence),
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    rol VARCHAR(10) NOT NULL,
    enabled BIT DEFAULT 1,  -- Cambiado a BIT para MSSQL
    multa_pendiente DECIMAL(10,2) DEFAULT 0.0  -- Mejor usar DECIMAL para dinero
);

-- Tabla de categorías
CREATE TABLE categoria (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR hibernate_sequence),
    nombre VARCHAR(120) UNIQUE NOT NULL
);

-- Tabla de autores
CREATE TABLE autor (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR hibernate_sequence),
    nombre VARCHAR(100) NOT NULL,
    pais VARCHAR(50) NOT NULL
);

-- Tabla de libros
CREATE TABLE libro (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR hibernate_sequence),
    titulo VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    anio_publicacion INTEGER,
    descripcion TEXT,
    unidades INTEGER NOT NULL DEFAULT 0,
    categoria_id BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Tabla de relación libro-autor
CREATE TABLE libro_autor (
    libro_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (libro_id, autor_id),
    FOREIGN KEY (libro_id) REFERENCES libro(id) ON DELETE CASCADE,
    FOREIGN KEY (autor_id) REFERENCES autor(id) ON DELETE CASCADE
);

-- Tabla de ejemplares
CREATE TABLE ejemplar (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR hibernate_sequence),
    codigo VARCHAR(50) UNIQUE NOT NULL,
    libro_id BIGINT NOT NULL,
    prestado BIT DEFAULT 0,  -- Cambiado a BIT para MSSQL
    FOREIGN KEY (libro_id) REFERENCES libro(id)
);

-- Tabla de préstamos
CREATE TABLE prestamo (
    id BIGINT PRIMARY KEY DEFAULT (NEXT VALUE FOR hibernate_sequence),
    usuario_id BIGINT NOT NULL,
    ejemplar_id BIGINT NOT NULL,
    fecha_prestamo DATE NOT NULL DEFAULT GETDATE(),
    fecha_devolucion DATE,
    fecha_vence DATE NOT NULL,
    multa DECIMAL(10,2) DEFAULT 0.0,  -- Mejor usar DECIMAL para dinero
    estado VARCHAR(12) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (ejemplar_id) REFERENCES ejemplar(id)
);

-- Índices para mejorar rendimiento
CREATE INDEX IX_usuario_email ON usuario(email);
CREATE INDEX IX_usuario_username ON usuario(username);
CREATE INDEX IX_libro_categoria ON libro(categoria_id);
CREATE INDEX IX_ejemplar_libro ON ejemplar(libro_id);
CREATE INDEX IX_prestamo_usuario ON prestamo(usuario_id);
CREATE INDEX IX_prestamo_ejemplar ON prestamo(ejemplar_id);