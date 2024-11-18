.386
.MODEL flat, stdcall
option casemap :none
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG

.data
__cadenita__ DB "me siento identificado con un string", 10, 0
__new_line__ DB 13, 10, 0 ; CRLF
auxt_13 DQ ?    ; por como funciona printf hay q declararla con 64 bits
u1@main@f1 DWORD 15


.code
start:
fild u1@main@f1 
fstp auxt_13

;invoke StdOut, addr __cadenita__
invoke printf, ADDR __cadenita__

INVOKE printf, addr __new_line__
invoke printf, cfm$("%.20Lf\n"), auxt_13

end start