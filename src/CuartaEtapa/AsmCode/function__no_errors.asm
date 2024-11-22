.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include\masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
P@MAIN@INCREASE DW ?
P_PLUS_1@MAIN DW ?
P@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursión directa ni indirecta.", 0
chk_rec BYTE 0
auxt_1 DW ?
aux_charch_7  DB "VALOR ANTES DE LA FUNCION@ ",  0
auxt_9 DWORD ?
auxt_10 DW ?
aux_charch_12  DB "VALOR LUEGO DE LA FUNCION@ ",  0

.code
OverflowMul:
invoke StdOut, addr errorOverflowMul
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
OverflowSub:
invoke StdOut, addr errorOverflowSub
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
RecursiveAttempt:
invoke StdOut, addr errorRecursiveAttempt
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion

INCREASE@MAIN PROC P@MAIN:WORD
MOV auxt_1,P@MAIN@INCREASE
ADD auxt_1,1
MOV AX, auxt_1
MOV P@MAIN@INCREASE, AX
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u
"), P@MAIN@INCREASE
MOV EAX, P@MAIN@INCREASE
ret
ret
INCREASE@MAIN ENDP

start:

MOV AX, 0
MOV P@MAIN, AX
INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_7
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u
"), P@MAIN
MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
CALL INCREASE@MAIN
MOV auxt_10,0
ADD auxt_10,auxt_9
MOV AX, auxt_10
MOV P_PLUS_1@MAIN, AX
INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_12
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u
"), P_PLUS_1@MAIN

end start


