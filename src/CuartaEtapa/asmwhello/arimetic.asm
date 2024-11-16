.386
.model flat, stdcall
; .stack 4096 ?
option casemap :none

includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib

;Instrucción	Significado	Tamaño	Ejemplo de uso
;DB	Define Byte	1 byte (8 bits)	Caracteres o números pequeños
;DW	Define Word	2 bytes (16 bits)	Enteros más grandes
;DD	Define Double Word	4 bytes (32 bits)	Enteros o floats
;DQ	Define Quad Word	8 bytes (64 bits)	Valores muy grandes o precisos

; Operaciones aritméticas

.data
; TS pasada a registros



; supongo anda bien la semantica,y chequea la suma se haga entre variables q existen (regs existen)

; SUMA -> ['+',op1,op2] donde op1={CTE,ID,INV_FUNCION,ACC_PAIR,TERCETO (varaux)}
;[3] Terceto{operacion='SUMA', op1='U1', op2='U2', subtipo='UINTEGER'}
;en java se genera assembler para uinteger o para float, mirando el tipo del terceto.

; uinteger:
.data
u1 DW ? ; ya tiene valor de antes 
u2 DW ?
auxsum DW ? 
.code
MOV EAX, u1
ADD EAX, u2 
MOV auxsum, EAX ;queda la aux y sera usada  ( o no)
; pero como sabe el terceto que la use (ej una asignacion) que esa es la aux? es logica en java asumo

;float:
.data
u1 REAL4 ?
u2 REAL4 ?
auxsum REAL4 ?
; buffer DB 32 DUP(0) por si lo quiero convertir a cadena
.code
FLD u1 ; cargo a pila el float
FADD u2 ; sumo a lo de la pila, u2
FSTP auxsum ; guardo el resultado en auxsum ( en memoria)

;INVOKE ftoa, auxsum, ADDR buffer
;INVOKE StdOut, ADDR buffer


;single es como uinteger o float, el assembler usa el campo referido, la logica anterior se maneja en semantica

; funcion?
; en java antes de generar me fijo es una funcion alguno de los componentes?
; y ahi solo queda llamarla y usar el resultado. sencillo.
; genero previamente una asignacion entre la aux y la invocacion?
; ver tema parametro, copia valor?
; .data y .code esta hecho, es uinteger o single dependiendo lo q devulva la funcion



; otro terceto (variable aux)