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
@INCREASE@MAIN DW ?
P_PLUS_1@MAIN DW ?
P@MAIN DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_1 DW ?
aux_charch_8  DB "VALOR ANTES DE LA FUNCION@ ",  0
auxt_11 DW ?
aux_charch_13  DB "VALOR LUEGO DE LA FUNCION@ ",  0

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

INCREASE@MAIN PROC P@MAIN@INCREASE:WORD

MOV AX, P@MAIN@INCREASE
ADD AX, 1
MOV auxt_1, AX

MOV AX, auxt_1
MOV P@MAIN@INCREASE, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), P@MAIN@INCREASE

MOV AX, P@MAIN@INCREASE
MOV @INCREASE@MAIN, AX

ret

ret
INCREASE@MAIN ENDP

start:



MOV AX, 0
MOV P@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_8

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), P@MAIN

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke INCREASE@MAIN, P@MAIN

MOV AX, 0
ADD AX, @INCREASE@MAIN
MOV auxt_11, AX

MOV AX, auxt_11
MOV P_PLUS_1@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_13

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), P_PLUS_1@MAIN

end start


