DROP DATABASE IF EXISTS pokemon;
CREATE DATABASE pokemon;
USE pokemon;
-- 1. Catálogo de Tipos (Fuego, Agua, Volador, etc.)
CREATE TABLE tipo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE
);

-- 2. Catálogo de Movimientos / Ataques
CREATE TABLE movimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    id_tipo INT,
    potencia INT NULL, -- Algunos movimientos no hacen daño directo (ej. Malicioso)
    precision_atq INT NULL,
    pp INT NOT NULL,
    FOREIGN KEY (id_tipo) REFERENCES tipo(id)
);

-- 3. Pokédex (Datos fijos de cada especie de Pokémon)
CREATE TABLE pokedex (
    num_pokedex INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    hp_base INT NOT NULL,
    ataque_base INT NOT NULL,
    defensa_base INT NOT NULL,
    velocidad_base INT NOT NULL
);

-- Tabla intermedia para soportar Pokémon de DOBLE TIPO (ej. Fuego/Volador)
CREATE TABLE pokedex_tipo (
    num_pokedex INT,
    id_tipo INT,
    PRIMARY KEY (num_pokedex, id_tipo),
    FOREIGN KEY (num_pokedex) REFERENCES pokedex(num_pokedex) ON DELETE CASCADE,
    FOREIGN KEY (id_tipo) REFERENCES tipo(id) ON DELETE CASCADE
);

-- 4. Entrenadores
CREATE TABLE entrenador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    ciudad VARCHAR(50) NOT NULL
);

-- 5. Pokémons Individuales (Instancias reales capturadas)
CREATE TABLE pokemon_individual (
    id INT AUTO_INCREMENT PRIMARY KEY,
    num_pokedex INT NOT NULL,
    id_entrenador INT NULL, -- NULL si está salvaje
    mote VARCHAR(50),      -- Nombre personalizado dado por el entrenador
    nivel INT DEFAULT 1 CHECK (nivel BETWEEN 1 AND 100),
    hp_actual INT NOT NULL,
    genero ENUM('Macho', 'Hembra', 'Sin Género') NOT NULL,
    FOREIGN KEY (num_pokedex) REFERENCES pokedex(num_pokedex),
    FOREIGN KEY (id_entrenador) REFERENCES entrenador(id) ON DELETE SET NULL
);

-- Tabla intermedia para los 4 movimientos que puede tener cada Pokémon
CREATE TABLE pokemon_movimiento (
    id_pokemon INT,
    id_movimiento INT,
    PRIMARY KEY (id_pokemon, id_movimiento),
    FOREIGN KEY (id_pokemon) REFERENCES pokemon_individual(id) ON DELETE CASCADE,
    FOREIGN KEY (id_movimiento) REFERENCES movimiento(id) ON DELETE CASCADE
);


-- /////////////////////////////////////////////////////////
-- INSERCIÓN DE DATOS DE PRUEBA REALISTAS
-- /////////////////////////////////////////////////////////

-- Insertar Tipos
INSERT INTO tipo (nombre) VALUES ('Fuego'), ('Agua'), ('Eléctrico'), ('Volador'), ('Planta');

-- Insertar Movimientos
INSERT INTO movimiento (nombre, id_tipo, potencia, precision_atq, pp) VALUES 
('Impactrueno', 3, 40, 100, 30),
('Lanzallamas', 1, 90, 100, 15),
('Pistola Agua', 2, 40, 100, 25),
('Tajo Aéreo', 4, 75, 95, 15);

-- Insertar Especies en la Pokédex
INSERT INTO pokedex (num_pokedex, nombre, hp_base, ataque_base, defensa_base, velocidad_base) VALUES 
(6, 'Charizard', 78, 84, 78, 100),
(25, 'Pikachu', 35, 55, 40, 90),
(7, 'Squirtle', 44, 48, 65, 43);

-- Asignar Tipos a las Especies (Charizard es Fuego/Volador)
INSERT INTO pokedex_tipo (num_pokedex, id_tipo) VALUES 
(6, 1), (6, 4), -- Charizard: Fuego (1) y Volador (4)
(25, 3),        -- Pikachu: Eléctrico (3)
(7, 2);         -- Squirtle: Agua (2)

-- Insertar Entrenador
INSERT INTO entrenador (nombre, ciudad) VALUES ('Ash Ketchum', 'Pueblo Paleta');

-- Crear los Pokémon específicos de Ash
-- Pikachu nivel 50 (Mote: "Pika")
INSERT INTO pokemon_individual (num_pokedex, id_entrenador, mote, nivel, hp_actual, genero) 
VALUES (25, 1, 'Pika', 50, 110, 'Macho');

-- Charizard nivel 70 (Sin mote, se queda el nombre de la especie)
INSERT INTO pokemon_individual (num_pokedex, id_entrenador, mote, nivel, hp_actual, genero) 
VALUES (6, 1, NULL, 70, 220, 'Macho');

-- Asignar movimientos a los Pokémon creados
INSERT INTO pokemon_movimiento (id_pokemon, id_movimiento) VALUES 
(1, 1), -- Pikachu tiene Impactrueno
(2, 2), -- Charizard tiene Lanzallamas
(2, 4); -- Charizard tiene Tajo Aéreo


-- /////////////////////////////////////////////////////////
-- CONSULTAS DE PRUEBA REALISTAS
-- /////////////////////////////////////////////////////////

-- 1. Ver el equipo de Ash con sus tipos (Une los tipos dinámicamente)
SELECT 
    e.nombre AS Entrenador,
    COALESCE(p_ind.mote, p_dex.nombre) AS Pokemon,
    p_ind.nivel,
    GROUP_CONCAT(t.nombre SEPARATOR ' / ') AS Tipos
FROM pokemon_individual p_ind
JOIN pokedex p_dex ON p_ind.num_pokedex = p_dex.num_pokedex
JOIN entrenador e ON p_ind.id_entrenador = e.id
JOIN pokedex_tipo pt ON p_dex.num_pokedex = pt.num_pokedex
JOIN tipo t ON pt.id_tipo = t.id
WHERE e.nombre = 'Ash Ketchum'
GROUP BY p_ind.id;

-- 2. Ver los movimientos que conoce cada Pokémon del equipo
SELECT 
    COALESCE(p_ind.mote, p_dex.nombre) AS Pokemon,
    m.nombre AS Movimiento,
    t.nombre AS Tipo_Movimiento,
    m.potencia
FROM pokemon_individual p_ind
JOIN pokedex p_dex ON p_ind.num_pokedex = p_dex.num_pokedex
JOIN pokemon_movimiento pm ON p_ind.id = pm.id_pokemon
JOIN movimiento m ON pm.id_movimiento = m.id
JOIN tipo t ON m.id_tipo = t.id;