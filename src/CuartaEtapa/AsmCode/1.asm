.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include \masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
X@MAIN@FUN1@FUN2 DW ?
R@MAIN REAL4 ?
U2@MAIN@FUN1 DW ?
U1@MAIN@FUN1 DW ?
P@MAIN@FUN1 DW ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
auxt_3 DW ?
auxt_10 DW ?

.code
OverflowMul:
invoke StdOut, addr errorOverflowMul
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
OverflowSub:
invoke StdOut, addr errorOverflowSub
invoke ExitProcess, 1   ; tiene que terminar con la ejecucion
FUN1@MAIN PROC P@MAIN:WORD
FUN2@MAIN@FUN1 PROC X@MAIN@FUN1:WORD
MOV EAX, X
ret
ret
FUN2@MAIN@FUN1 ENDP
MOV EAX, U1
ret
ret
FUN1@MAIN ENDP

start:
MOV AX, 1
MOV U1@MAIN@FUN1, AX
MOV AX, 2
MOV U2@MAIN@FUN1, AX
MOV auxt_3,U1@MAIN@FUN1
ADD auxt_3,U2@MAIN@FUN1
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), auxt_3
INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), X@MAIN@FUN1@FUN2
MOV auxt_10,U1@MAIN@FUN1
ADD auxt_10,U2@MAIN@FUN1
MOV AX, auxt_10
MOV P@MAIN@FUN1, AX
fld varfloat
fstp varfloat

end start
