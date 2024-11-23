.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include\masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG
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
@FUN2@MAIN@FUN1 DD ?
AUX@MAIN REAL4 ?
@FUN1@MAIN DD ?
@FUN3@MAIN@FUN1@FUN2 DD ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_float_0 REAL4 0.0
.data
@FUN2@MAIN@FUN1 DD ?
AUX@MAIN REAL4 ?
@FUN1@MAIN DD ?
@FUN3@MAIN@FUN1@FUN2 DD ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_16_64 64B ?
aux_float_0 REAL4 0.0

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

FUN1@MAIN PROC X1@MAIN@FUN1:WORD

JMP TAG2@@MAIN@FUN1

TAG2@@MAIN@FUN1:


INVOKE printf, addr __new_line__
fld AUX@MAIN
fst auxt_16_64
invoke printf, cfm$("%.20Lf\n") auxt_16_64

ret
FUN1@MAIN ENDP

FUN2@MAIN@FUN1 PROC X1@MAIN@FUN1@FUN2:WORD

UNREACHABLE_FROM_MAIN@@MAIN@FUN1@FUN2:


MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke FUN3@MAIN@FUN1@FUN2, X1@MAIN@FUN1@FUN2

fld @FUN3@MAIN@FUN1@FUN2
fstp AUX@MAIN

TAG2@@MAIN@FUN1@FUN2:

ret
FUN2@MAIN@FUN1 ENDP

FUN3@MAIN@FUN1@FUN2 PROC X1@MAIN@FUN1@FUN2@FUN3:WORD

JMP TAG2@@MAIN@FUN1@FUN2

ret
FUN3@MAIN@FUN1@FUN2 ENDP

start:


fld aux_float_0
fstp AUX@MAIN

TAG1@@MAIN:

TAG3@@MAIN:

TAG2@@MAIN:


JMP TAG2@@MAIN
start:



JMP TAG1@@MAIN
fld aux_float_0
fstp AUX@MAIN

JMP TAG3@@MAIN

TAG1@@MAIN:


TAG3@@MAIN:
end start


.386                  ; Enable 32-bit instructions
.MODEL FLAT,STDCALL           ; Use FLAT memory model for 32-bit systems
.STACK 100h           ; Define stack size;
option casemap :none
include\masm32\include\masm32rt.inc
includelib\masm32\lib\kernel32.lib
includelib\masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG
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
@FUN2@MAIN@FUN1 DD ?
AUX@MAIN REAL4 ?
@FUN1@MAIN DD ?
@FUN3@MAIN@FUN1@FUN2 DD ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
aux_float_0 REAL4 0.0
.data
@FUN2@MAIN@FUN1 DD ?
AUX@MAIN REAL4 ?
@FUN1@MAIN DD ?
@FUN3@MAIN@FUN1@FUN2 DD ?
__new_line__ DB 13, 10, 0 ; CRLF
errorOverflowMul db "ERROR: Overflow detectado! Una multiplicacion de enteros excede el limite de 16 bits", 0
errorOverflowSub db "ERROR: Overflow detectado! Una resta de enteros da negativo", 0
errorRecursiveAttempt db "ERROR: Llamado recursivo detectado! No se permite la recursion directa ni indirecta.", 0
chk_rec BYTE 0
auxt_16_64 64B ?
aux_float_0 REAL4 0.0
auxt_16_64 64B ?

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

FUN1@MAIN PROC X1@MAIN@FUN1:WORD

JMP TAG2@@MAIN@FUN1

TAG2@@MAIN@FUN1:


INVOKE printf, addr __new_line__
fld AUX@MAIN
fst auxt_16_64
invoke printf, cfm$("%.20Lf\n") auxt_16_64

ret
FUN1@MAIN ENDP

FUN2@MAIN@FUN1 PROC X1@MAIN@FUN1@FUN2:WORD

UNREACHABLE_FROM_MAIN@@MAIN@FUN1@FUN2:


MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke FUN3@MAIN@FUN1@FUN2, X1@MAIN@FUN1@FUN2

fld @FUN3@MAIN@FUN1@FUN2
fstp AUX@MAIN

TAG2@@MAIN@FUN1@FUN2:

ret
FUN2@MAIN@FUN1 ENDP

FUN3@MAIN@FUN1@FUN2 PROC X1@MAIN@FUN1@FUN2@FUN3:WORD

JMP TAG2@@MAIN@FUN1@FUN2

ret
FUN3@MAIN@FUN1@FUN2 ENDP

start:


fld aux_float_0
fstp AUX@MAIN

TAG1@@MAIN:

TAG3@@MAIN:

TAG2@@MAIN:


JMP TAG2@@MAIN
start:



JMP TAG1@@MAIN
fld aux_float_0
fstp AUX@MAIN

JMP TAG3@@MAIN

TAG1@@MAIN:


TAG3@@MAIN:
end start


FUN1@MAIN PROC X1@MAIN@FUN1:WORD

JMP TAG2@@MAIN@FUN1

TAG2@@MAIN@FUN1:


INVOKE printf, addr __new_line__
fld AUX@MAIN
fst auxt_16_64
invoke printf, cfm$("%.20Lf\n") auxt_16_64

ret
FUN1@MAIN ENDP
FUN1@MAIN PROC X1@MAIN@FUN1:WORD

JMP TAG2@@MAIN@FUN1

TAG2@@MAIN@FUN1:


INVOKE printf, addr __new_line__
fld AUX@MAIN
fst auxt_16_64
invoke printf, cfm$("%.20Lf\n") auxt_16_64

ret
FUN1@MAIN ENDP

FUN2@MAIN@FUN1 PROC X1@MAIN@FUN1@FUN2:WORD

UNREACHABLE_FROM_MAIN@@MAIN@FUN1@FUN2:


MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke FUN3@MAIN@FUN1@FUN2, X1@MAIN@FUN1@FUN2

fld @FUN3@MAIN@FUN1@FUN2
fstp AUX@MAIN

TAG2@@MAIN@FUN1@FUN2:

ret
FUN2@MAIN@FUN1 ENDP
FUN2@MAIN@FUN1 PROC X1@MAIN@FUN1@FUN2:WORD

UNREACHABLE_FROM_MAIN@@MAIN@FUN1@FUN2:


MOV chk_rec, 0
CMP chk_rec, 0
JNZ RecursiveAttempt
invoke FUN3@MAIN@FUN1@FUN2, X1@MAIN@FUN1@FUN2

fld @FUN3@MAIN@FUN1@FUN2
fstp AUX@MAIN

TAG2@@MAIN@FUN1@FUN2:

ret
FUN2@MAIN@FUN1 ENDP

FUN3@MAIN@FUN1@FUN2 PROC X1@MAIN@FUN1@FUN2@FUN3:WORD

JMP TAG2@@MAIN@FUN1@FUN2

ret
FUN3@MAIN@FUN1@FUN2 ENDP

start:


fld aux_float_0
fstp AUX@MAIN

TAG1@@MAIN:

TAG3@@MAIN:

TAG2@@MAIN:


JMP TAG2@@MAIN
start:



JMP TAG1@@MAIN
fld aux_float_0
fstp AUX@MAIN

JMP TAG3@@MAIN

TAG1@@MAIN:


TAG3@@MAIN:
end start

TAG2@@MAIN:


JMP TAG2@@MAIN

JMP TAG1@@MAIN

JMP TAG3@@MAIN

end start


