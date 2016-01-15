package com.rameses.common;

import com.rameses.util.Service;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ExpressionResolver {
    
    
    private Pattern pattern = Pattern.compile("@.*?\\(");
    private Map<String,String> functions;
    
    private String findFunction(String key) {
        return FunctionResolver.getInstance().findStringFunction( key );
        /*
        if( functions == null ) {
            try {
                LineReader reader = new LineReader();
                final Map _xfunc = new Hashtable();
                Enumeration<URL> urls = getClass().getClassLoader().getResources("META-INF/functions");
                while(urls.hasMoreElements()) {
                    URL u = urls.nextElement();
                    InputStream is = u.openStream();
                    reader.read(is, new LineReader.Handler(){
                        public void read(String text) {
                            String[] arr = text.split("=");
                            _xfunc.put( arr[0].trim(), arr[1].trim() );
                        }
                    });
                }
                functions = _xfunc;
            }
            catch(RuntimeException re) {
                throw re;
            }
            catch(Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        String f = functions.get(key);
        if(f==null)
            throw new RuntimeException("Function " + key + " not found");
        return f;
         */
    }
    
    protected String replaceFunctions(String text) {
        text = text.replaceAll("#\\{", "\\$\\{").replace(" and "," && ").replaceAll(" or ", " || ");
        Matcher m = pattern.matcher(text);
        StringBuilder sb = new StringBuilder();
        int start = 0;
        while(m.find()) {
            sb.append( text.substring(start, m.start()) );
            String stext = text.substring(m.start(),m.end()).trim();
            String _pre = stext.substring(1, stext.indexOf("("));
            sb.append( findFunction(_pre)+"(" );
            start = m.end();
        }
        if( start < text.length()  ) sb.append( text.substring(start));
        return sb.toString();
    }
    
    
    public abstract Object eval(String expression, Object o);
    public abstract String evalString(String expression, Object o);
    public abstract boolean evalBoolean(String expression, Object o);
    public abstract double evalDouble(String expression, Object o);
    public abstract int evalInt(String expression, Object o);
    public abstract BigDecimal evalDecimal(String expression, Object o);
        
    //implementation
    private static ExpressionResolver instance;
    
    public static ExpressionResolver getInstance() {
        if(instance==null) {
            Iterator e = Service.providers(ExpressionResolver.class,ExpressionResolver.class.getClassLoader());
            if(e.hasNext()) {
                instance = (ExpressionResolver)e.next();
            }
        }
        if(instance==null)
            throw new RuntimeException("There is no expression resolver specified");
        return instance;
    }
    
    
}
