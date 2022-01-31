# Programa que recibe paquetes http de un cliente y los redirecciona a un servidor (Puente HTTP)

import socket
import time

ip_server = "192.168.249.128"
port_server = 8080

ip_host = "192.168.1.132"
port_host = 8080

timeout = 2

# Creamos el socket
host = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Aceptamos conexiones entrantes
host.bind((ip_host, port_host))
host.listen(5)

while True:
    # Aceptamos conexiones entrantes
    client, addr_client = host.accept()
    client.settimeout(timeout)
    print("="*50)
    print('Conexion desde: ', addr_client[0], ':', addr_client[1])

    try:
        # Recibimos datos
        data = client.recv(65535)
        print('Recibidos', len(data), 'bytes desde el cliente')
    except socket.timeout:
        print('Timeout al enviar al servidor')
        client.close()
        continue

    # Creamos el socket con el servidor
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Conectamos con el servidor
    server.connect((ip_server, port_server))
    server.settimeout(timeout)
    print('Conectado al servidor en: ', ip_server, ':', port_server)
    # Enviamos datos al servidor
    server.sendall(data)
    print("Enviados",len(data),"bytes al servidor")

    try:
        # Recibimos datos del servidor
        data2 = server.recv(65535)
        print('Recibidos', len(data2), 'bytes desde el servidor')
    except:
        print('Timeout al recibir del servidor')
        server.close()
        client.close()
        continue

    # Cerramos conexion con servidor
    server.close()
    print('Cerrando conexion con servidor en: ', ip_server, ':', port_server)

    # Enviamos datos al cliente
    
    client.sendall(data2)
    print("Enviados",len(data2),"bytes al cliente")

    # Cerramos conexion con cliente
    client.close()
    print("Conexion cerrada con ", addr_client[0], ":", addr_client[1])
    time.sleep(1)
