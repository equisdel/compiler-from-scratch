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
Z@MAIN REAL4 ?
A@MAIN DW ?
B@MAIN DW ?
X@MAIN REAL4 ?
C@MAIN DW ?
Y@MAIN REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_charch_0  DB "INICIO ",  0
aux_float_7 DD 1.0
aux_float_8 DD 2.0
auxt_9 REAL4 ?
aux_float_9 DD 3
auxt_11_64 DQ ?
auxt_12_64 DQ ?
auxt_13_64 DQ ?

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

start:


INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_0

MOV AX, 1
MOV A@MAIN, AX

MOV AX, 2
MOV B@MAIN, AX

MOV AX, 3
MOV C@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), A@MAIN

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), B@MAIN

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), C@MAIN

fld aux_float_7
fstp X@MAIN

fld aux_float_8
fstp Y@MAIN

fild aux_float_9
fstp auxt_9

fld auxt_9
fstp Z@MAIN

INVOKE printf, addr __new_line__
fld X@MAIN
fst auxt_11_64
invoke printf, cfm$("%.20Lf\n"), auxt_11_64

INVOKE printf, addr __new_line__
fld Y@MAIN
fst auxt_12_64
invoke printf, cfm$("%.20Lf\n"), auxt_12_64

INVOKE printf, addr __new_line__
fld Z@MAIN
fst auxt_13_64
invoke printf, cfm$("%.20Lf\n"), auxt_13_64

invoke printf, addr __new_line__

end start


