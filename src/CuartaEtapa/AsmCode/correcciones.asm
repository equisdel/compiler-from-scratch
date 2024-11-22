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
S@MAIN REAL4 ?
TESTVERYVERYLAR@MAIN REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_float_0 REAL4 0.0
aux_float_1 REAL4 0.0
aux_float_2 REAL4 0.1S1
aux_float_3 REAL4 1.1S+1
aux_float_4 REAL4 2.2S-2
aux_float_5 REAL4 3.3S3
aux_float_6 REAL4 44.44S+44
aux_float_7 REAL4 55.555
aux_float_8 REAL4 -0.0
aux_float_9 REAL4 -0.1S-1
aux_float_10 REAL4 -1.1S+1
aux_float_11 REAL4 -2.2S-2
aux_float_12 REAL4 -3.3S3
aux_float_13 REAL4 -44.44S+44
aux_float_14 REAL4 -55.555

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

fld aux_float_0
fstp TESTVERYVERYLAR@MAIN
fld aux_float_1
fstp S@MAIN
fld aux_float_2
fstp S@MAIN
fld aux_float_3
fstp S@MAIN
fld aux_float_4
fstp S@MAIN
fld aux_float_5
fstp S@MAIN
fld aux_float_6
fstp S@MAIN
fld aux_float_7
fstp S@MAIN
fld aux_float_8
fstp S@MAIN
fld aux_float_9
fstp S@MAIN
fld aux_float_10
fstp S@MAIN
fld aux_float_11
fstp S@MAIN
fld aux_float_12
fstp S@MAIN
fld aux_float_13
fstp S@MAIN
fld aux_float_14
fstp S@MAIN

end start


