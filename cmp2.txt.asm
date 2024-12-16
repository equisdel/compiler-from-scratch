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
DC@MAIN REAL4 ?
DB@MAIN REAL4 ?
DA@MAIN REAL4 ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_float_0 DD 1.0
aux_float_1 DD 2.0
aux_float_2 DD 3.0
auxt_3 REAL4 ?
aux_float_3_bis DD 2.0
auxt_4 REAL4 ?
auxt_5 REAL4 ?
aux_float_5_bis DD 2.0
auxt_6 REAL4 ?
auxt_7 REAL4 ?
aux_float_7_bis DD 4.0
aux_float_9 DD 11.0
aux_charch_11  DB "IGUAL ",  0
aux_float_12 DD 10.0
aux_charch_14  DB "MAYOR ",  0
aux_float_15 DD 11.0
aux_charch_17  DB "MAYOR O IGUAL ",  0
aux_float_18 DD 12.0
aux_charch_20  DB "MENOR ",  0
aux_float_21 DD 11.0
aux_charch_23  DB "MENOR O IGUAL ",  0
auxt_24 REAL4 ?
aux_float_24 DD 1.2
aux_float_24_bis DD 2.4
auxt_26_64 DQ ?

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
fstp DA@MAIN

fld aux_float_1
fstp DB@MAIN

fld aux_float_2
fstp DC@MAIN

fld DC@MAIN
fmul aux_float_3_bis
fstp auxt_3

fld DB@MAIN
fld auxt_3
fadd
fstp auxt_4

MOV DX, 0
fld DB@MAIN
fdiv aux_float_5_bis
fstp auxt_5

fld auxt_4
fsub auxt_5
fstp auxt_6

fld auxt_6
fld aux_float_7_bis
fadd
fstp auxt_7

fld auxt_7
fstp DA@MAIN

fld DA@MAIN
fld aux_float_9
fcom
fstsw AX
sahf
finit

JNE labelt_12

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_11

labelt_12:
fld DA@MAIN
fld aux_float_12
fcom
fstsw AX
sahf
finit

JA labelt_15

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_14

labelt_15:
fld DA@MAIN
fld aux_float_15
fcom
fstsw AX
sahf
finit

JB labelt_18

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_17

labelt_18:
fld DA@MAIN
fld aux_float_18
fcom
fstsw AX
sahf
finit

JB labelt_21

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_20

labelt_21:
fld DA@MAIN
fld aux_float_21
fcom
fstsw AX
sahf
finit

JA labelt_24

INVOKE printf, addr __new_line__
invoke printf, addr aux_charch_23

labelt_24:
fld aux_float_24
fld aux_float_24_bis
fadd
fstp auxt_24

fld auxt_24
fstp DA@MAIN

INVOKE printf, addr __new_line__
fld DA@MAIN
fst auxt_26_64
invoke printf, cfm$("%.20Lf\n"), auxt_26_64

invoke printf, addr __new_line__

end start


