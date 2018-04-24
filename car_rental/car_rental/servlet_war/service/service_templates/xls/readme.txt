help for invoke_method:
-----------------------

to set param or extra values in list must use:
%invoke_methodthis.getServletParam().getExtraList().put((java.lang.Object)"report",(java.lang.Object)getServletParam().getParamList().get(<PARAM_NUMBER>))%invoke_method

Can set %param<PARAM_NUMBER>%param if parameter not contains character: " (needs to reset as \")
%invoke_methodthis.getServletParam().getExtraList().put((java.lang.Object)"report",(java.lang.Object)"%param20%param")%invoke_method

Can not use "this" pointer

