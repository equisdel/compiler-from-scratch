.386                  ; Enable 32-bit instructions
.MODEL FLAT           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;

.data
INCREASE:MAIN DW ?
P:MAIN:INCREASE DW ?
P_PLUS_1:MAIN DW ?
P:MAIN DW ?
auxt_1 DW ?
auxt_8 DW ?

.code
CALL INCREASE@MAIN:

start:
INCREASE@MAIN PROC
XOR EAX, EAX
MOV EAX, [ESP+4]
MOV auxt_1,P@MAIN@INCREASE
ADD auxt_1,1
MOV P@MAIN@INCREASE,auxt_1
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), P@MAIN@INCREASE
MOV P@MAIN,0
INVOKE printf, addr __new_line__
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), P@MAIN
MOV auxt_8,0
ADD auxt_8,auxt_7
MOV P_PLUS_1@MAIN,auxt_8
INVOKE printf, addr __new_line__
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), P_PLUS_1@MAIN

end start
