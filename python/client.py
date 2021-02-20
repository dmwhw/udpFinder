#!/usr/bin/python
#coding=utf-8
#发送端
import sys,struct,socket
from time import sleep
import traceback

message="hello"
message1="nihao"

multicast_group=('224.3.29.72',10003)
multicast_group1=('224.3.29.72',10004)

sock=socket.socket(socket.AF_INET,socket.SOCK_DGRAM)

#sock.timeout(3)

ttl=struct.pack('b',1)  #将数字1转换成无符号字符类型.在python中没有这种类型,但是内核需要,所以转换

sock.setsockopt(socket.IPPROTO_IP,socket.IP_MULTICAST_TTL,ttl)

try:
    while 1:
        sleep(2)
        sent=sock.sendto(message,multicast_group)
        sent=sock.sendto(message1,multicast_group1)

        print >>sys.stderr,"waiting to receive"

        try:
            data,server=sock.recvfrom(64)
        except Exception as e:
            traceback.print_exc()
            print ("time out ,no more response")
              
            break
        else:
            print >>sys.stderr,"received %s from %s"%(data,server)
finally:
    print >>sys.stderr,"closing socket"
    sock.close()