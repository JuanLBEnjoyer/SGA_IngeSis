-- ============================================================
-- Datos iniciales para desarrollo y verificación H2
-- Se ejecuta automáticamente al arrancar con ddl-auto=create-drop
-- ============================================================

-- Usuarios de prueba
INSERT INTO usuarios (numero_documento, tipo_documento, nombre, email, rol) VALUES
('1094123456', 'CEDULA', 'Juan Pablo Galeano', 'jpgaleano@uqvirtual.edu.co',       'ESTUDIANTE'),
('987654321',  'CEDULA', 'Daniel Garcia',      'daniel.garcia@uniquindio.edu.co',   'ADMINISTRATIVO'),
('111222333',  'CEDULA', 'Maria Lopez',        'maria.lopez@uniquindio.edu.co',     'DOCENTE'),
('444555666',  'CEDULA', 'Carlos Ramirez',     'carlos.ramirez@uniquindio.edu.co',  'DIRECTIVO');

-- Solicitudes de ejemplo
INSERT INTO solicitudes (codigo, tipo, descripcion, estado, prioridad,
                         solicitante_documento, solicitante_tipo_documento,
                         responsable_documento, responsable_tipo_documento)
VALUES
('001', 'REGISTRAR_ASIGNATURA', 'Necesito registrar la asignatura Programacion Avanzada para este semestre',
 'REGISTRADA', NULL, '1094123456', 'CEDULA', NULL, NULL),

('002', 'HOMOLOGACION', 'Solicito homologacion de la asignatura Algoritmos cursada en otra universidad',
 'CLASIFICADA', 'ALTO', '1094123456', 'CEDULA', NULL, NULL),

('003', 'CANCELACION_ASIGNATURA', 'Requiero cancelar la asignatura Calculo Diferencial por motivos de salud',
 'EN_ATENCION', 'MEDIO', '1094123456', 'CEDULA', '987654321', 'CEDULA');

-- Historial de la solicitud 001 (REGISTRADA)
INSERT INTO solicitud_historial (solicitud_id, orden, descripcion, fecha, estado_asociado) VALUES
(1, 0, 'Solicitud registrada', CURRENT_TIMESTAMP, 'REGISTRADA');

-- Historial de la solicitud 002 (CLASIFICADA)
INSERT INTO solicitud_historial (solicitud_id, orden, descripcion, fecha, estado_asociado) VALUES
(2, 0, 'Solicitud registrada',   CURRENT_TIMESTAMP, 'REGISTRADA'),
(2, 1, 'Solicitud clasificadaInicio de semestre, se requiere con urgencia', CURRENT_TIMESTAMP, 'CLASIFICADA');

-- Historial de la solicitud 003 (EN_ATENCION)
INSERT INTO solicitud_historial (solicitud_id, orden, descripcion, fecha, estado_asociado) VALUES
(3, 0, 'Solicitud registrada',             CURRENT_TIMESTAMP, 'REGISTRADA'),
(3, 1, 'Solicitud clasificadaDocumentacion completa y verificada', CURRENT_TIMESTAMP, 'CLASIFICADA'),
(3, 2, 'Solicitud asignada a responsable', CURRENT_TIMESTAMP, 'EN_ATENCION');