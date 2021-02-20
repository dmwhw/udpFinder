#!/usr/bin/python
#接收端
import socket
import sys
import struct

multicast_group = '239.255.255.2'
server_address = ('',6689)

sock = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)

sock.bind(server_address)

group = socket.inet_aton(multicast_group)
mreq = struct.pack('4sL',group,socket.INADDR_ANY)
sock.setsockopt(socket.IPPROTO_IP,socket.IP_ADD_MEMBERSHIP,mreq)

while True:
    print('\nwaiting to receive message')
    data,address = sock.recvfrom(64)

    print('received %s bytes from %s'%(len(data),data))
    print('sending acknowledgement to',address)
    sock.sendto('from server....'.encode("utf-8"),address)