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
AXE@MAIN REAL4 ?
@FUNCION2@MAIN DW ?
B@MAIN DW ?
X@MAIN DW ?
Y2@MAIN DW ?
AX@MAIN REAL4 ?
Z@MAIN DW ?
A@MAIN DW ?
A@MAIN@FUNCION2 REAL4 ?
X@MAIN@FUNCION1 DW ?
@FUNCION1@MAIN DD ?
A@MAIN@FUNCION1 DW ?
C@MAIN DW ?
PEPE@MAIN@FUNCION1 REAL4 ?
AXF@MAIN REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_charch_1  DB "EN FUN1 ",  0
auxt_2 REAL4 ?
aux_float_2 DD 1
auxt_3 REAL4 ?
auxt_5 DW ?
auxt_6 DW ?
auxt_7 REAL4 ?
auxt_8 REAL4 ?
auxt_10_64 DQ ?
auxt_11_64 DQ ?
auxt_12 REAL4 ?
aux_float_12 DD 1
auxt_13 REAL4 ?
aux_charch_18  DB "EN FUN22 ",  0
auxt_19 DW ?
auxt_22 REAL4 ?
auxt_26_64 DQ ?
aux_float_34 DD 1.9
auxt_38_64 DQ ?

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

FUNCION1@MAIN PROC PARAMETRO@MAIN@FUNCION1:DWORD

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_1

fild aux_float_2
fstp auxt_2

fld PARAMETRO@MAIN@FUNCION1
fld auxt_2
fadd
fstp auxt_3

fld auxt_3
fstp PARAMETRO@MAIN@FUNCION1

MOV AX ,B@MAIN
MOV CX ,X@MAIN@FUNCION1
MUL CX
CMP DX, 0
JNE OverflowMul
MOV auxt_5 ,AX

MOV AX, A@MAIN@FUNCION1
ADD AX, auxt_5
MOV auxt_6, AX

fild auxt_6
fstp auxt_7

fld auxt_7
fld PARAMETRO@MAIN@FUNCION1
fadd
fstp auxt_8

fld auxt_8
fstp PEPE@MAIN@FUNCION1

INVOKE printf, addr __new_line__
fld PARAMETRO@MAIN@FUNCION1
fst auxt_10_64
invoke printf, cfm$("%.20Lf\n"), auxt_10_64

INVOKE printf, addr __new_line__
fld PEPE@MAIN@FUNCION1
fst auxt_11_64
invoke printf, cfm$("%.20Lf\n"), auxt_11_64

fild aux_float_12
fstp auxt_12

fld PEPE@MAIN@FUNCION1
fld auxt_12
fadd
fstp auxt_13

fld auxt_13
fstp @FUNCION1@MAIN

ret

ret
FUNCION1@MAIN ENDP

FUNCION2@MAIN PROC PARAMETRO@MAIN@FUNCION2:WORD

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_18

MOV AX, PARAMETRO@MAIN@FUNCION2
ADD AX, 1
MOV auxt_19, AX

MOV AX, auxt_19
MOV PARAMETRO@MAIN@FUNCION2, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), PARAMETRO@MAIN@FUNCION2

fild PARAMETRO@MAIN@FUNCION2
fstp auxt_22

fld auxt_22
fstp AX@MAIN

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke FUNCION1@MAIN, AX@MAIN

fld @FUNCION1@MAIN
fstp A@MAIN@FUNCION2

INVOKE printf, addr __new_line__
fld A@MAIN@FUNCION2
fst auxt_26_64
invoke printf, cfm$("%.20Lf\n"), auxt_26_64

fld A@MAIN@FUNCION2
fstp AXF@MAIN

ret
FUNCION2@MAIN ENDP

start:




MOV AX, 2
MOV A@MAIN, AX

MOV AX, 1
MOV X@MAIN, AX

MOV AX, 2
MOV Y2@MAIN, AX

MOV AX, 3
MOV Z@MAIN, AX

MOV AX, 1
MOV C@MAIN, AX

fld aux_float_34
fstp AX@MAIN

MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke FUNCION2@MAIN, C@MAIN

MOV AX, @FUNCION2@MAIN
MOV B@MAIN, AX

INVOKE printf, addr __new_line__
invoke printf, cfm$("%u\n"), B@MAIN

INVOKE printf, addr __new_line__
fld AXF@MAIN
fst auxt_38_64
invoke printf, cfm$("%.20Lf\n"), auxt_38_64

invoke printf, addr __new_line__

end start


