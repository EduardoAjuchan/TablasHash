
CREATE DATABASE IF NOT EXISTS catalogo_paises;


USE catalogo_paises;

-- Tabla de Países
CREATE TABLE IF NOT EXISTS paises (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    iso_code CHAR(3) NOT NULL,
    poblacion INT,
    capital VARCHAR(255),
    
    UNIQUE (iso_code)
);

-- Tabla de Estados o Departamentos
CREATE TABLE IF NOT EXISTS estados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pais INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    
    FOREIGN KEY (id_pais) REFERENCES paises(id)
);

-- Tabla de Municipios
CREATE TABLE IF NOT EXISTS municipios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_estado INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
   
    FOREIGN KEY (id_estado) REFERENCES estados(id)
);
