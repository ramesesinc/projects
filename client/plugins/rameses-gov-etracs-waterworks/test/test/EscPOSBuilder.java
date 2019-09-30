/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author wflores
 */
public class EscPOSBuilder {
    
    private StringBuilder out;
    
    public EscPOSBuilder() {
        out = new StringBuilder();
    }
    
    public EscPOSBuilder append( Object value ) {
        if ( value instanceof String ) {
            out.append( value.toString()); 
        } else if ( value instanceof StringBuilder ) {
            out.append((StringBuilder) value); 
        } else if ( value instanceof CharSequence ) {
            out.append((CharSequence) value); 
        } else {
            out.append( value ); 
        }
        return this;
    }
    public EscPOSBuilder append( char[] chars ) {
        out.append( chars ); 
        return this;
    }
    public EscPOSBuilder append( char ch ) {
        out.append( ch ); 
        return this;
    }
    public EscPOSBuilder append( int num ) {
        out.append( num ); 
        return this;
    }
    public EscPOSBuilder append( long num ) {
        out.append( num ); 
        return this;
    }
    
    public String getText() {
        return out.toString();
    }
    
    public void clear() {
        out = new StringBuilder();
    }
   
    public void init() {
        out.append(new String(new char[]{ 0x1b, '@' })); 
    }
    
    public void carriageReturn() {
        out.append(new String(new char[]{ 0x0d })); 
    }

    public void formFeed() {
        out.append(new String(new char[]{ 0x0c })); 
    }

    public void lineFeed() {
        out.append(new String(new char[]{ 0x0a })); 
    }

    public void lineFeed( int lines ) {
        out.append(new String(new char[]{ 0x1b, 'd' })).append( lines );
    }
    
    public void setPageLengthInLines(int lines ) {
        out.append(new String(new char[]{ 0x1b, 'C' })).append( lines ); 
    }

    public void setPageLengthInInches(int inches ) {
        out.append(new String(new char[]{ 0x1b, 'C', 0x00 })).append( inches ); 
    }

    public void setMargin_Off() {
        out.append(new String(new char[]{ 0x1b, 0x4f })); 
    }
    
    public void setMarginBottom(int margin ) {
        out.append(new String(new char[]{ 0x1b, 'N' })).append( margin ); 
    }
    public void setMarginRight(int margin ) {
        out.append(new String(new char[]{ 0x1b, 0x51 })).append( margin ); 
    }
    public void setLeftRight(int margin ) {
        out.append(new String(new char[]{ 0x1b, 0x6c })).append( margin ); 
    }

    public void setLineSpacingMode_8() {
        out.append(new String(new char[]{ 0x1b, 0x30 })); 
    }
    public void setLineSpacingMode_6() {
        out.append(new String(new char[]{ 0x1b, 0x32 })); 
    }
    public void setLineSpacingMode_60( int num ) {
        out.append(new String(new char[]{ 0x1b, 0x41 })).append( num ); 
    }
    public void setLineSpacingMode_72( int num ) {
        out.append(new String(new char[]{ 0x1b, 0x41 })).append( num ); 
    }
    public void setLineSpacingMode_180( int num ) {
        out.append(new String(new char[]{ 0x1b, 0x33 })).append( num ); 
    }
    public void setLineSpacingMode_216( int num ) {
        out.append(new String(new char[]{ 0x1b, 0x33 })).append( num ); 
    }
    public void setLineSpacingMode_360( int num ) {
        out.append(new String(new char[]{ 0x1b, 0x2b })).append( num ); 
    }

    public void setAlignLeft() {
        out.append(new String(new char[]{ 0x1b, 'a', 0 })); 
    }
    public void setAlignCenter() {
        out.append(new String(new char[]{ 0x1b, 'a', 1 })); 
    }
    public void setAlignRight() {
        out.append(new String(new char[]{ 0x1b, 'a', 2 })); 
    }
    public void setAlignFull() {
        out.append(new String(new char[]{ 0x1b, 'a', 3 })); 
    }

    public void setLQ_On() {
        out.append(new String(new char[]{ 0x1b, 0x78, 1 })); 
    }
    public void setLQ_Off() {
        out.append(new String(new char[]{ 0x1b, 0x78, 0 })); 
    }

    public void setTypeFace_Roman() {
        out.append(new String(new char[]{ 0x1b, 0x6b, 0 })); 
    }
    public void setTypeFace_SansSerif() {
        out.append(new String(new char[]{ 0x1b, 0x6b, 1 })); 
    }
    public void setTypeFace_Courier() {
        out.append(new String(new char[]{ 0x1b, 0x6b, 2 })); 
    }
    public void setTypeFace( int opt ) {
        out.append(new String(new char[]{ 0x1b, 0x6b })).append( opt ); 
    }

    public void setCPI_10() {
        out.append(new String(new char[]{ 0x1b, 0x50 }));
    }
    public void setCPI_12() {
        out.append(new String(new char[]{ 0x1b, 0x4d }));
    }
    public void setCPI_15() {
        out.append(new String(new char[]{ 0x1b, 0x67 }));
    }
    
    public void setDoubleStrike_On() {
        out.append(new String(new char[]{ 0x1b, 0x47 }));
    }
    public void setDoubleStrike_Off() {
        out.append(new String(new char[]{ 0x1b, 0x48 }));
    }
    
    public void setDoubleWidth_On() {
        out.append(new String(new char[]{ 0x1b, 0x57, 1 }));
    }
    public void setDoubleWidth_Off() {
        out.append(new String(new char[]{ 0x1b, 0x57, 0 }));
    }
    
    public void setDoubleHeight_On() {
        out.append(new String(new char[]{ 0x1b, 0x77, 1 }));
    }
    public void setDoubleHeight_Off() {
        out.append(new String(new char[]{ 0x1b, 0x77, 0 }));
    }
    
    public void setBold_On() {
        out.append(new String(new char[]{ 0x1b, 0x45 }));
    }
    public void setBold_Off() {
        out.append(new String(new char[]{ 0x1b, 0x46 }));
    }

    public void setItalic_On() {
        out.append(new String(new char[]{ 0x1b, 0x34 }));
    }
    public void setItalic_Off() {
        out.append(new String(new char[]{ 0x1b, 0x35 }));
    }

    public void setUnderline_On() {
        out.append(new String(new char[]{ 0x1b, 0x2d, 1 }));
    }
    public void setUnderline_Off() {
        out.append(new String(new char[]{ 0x1b, 0x2d, 0 }));
    }

    public void setSuperscript_On() {
        out.append(new String(new char[]{ 0x1b, 0x53, 0 }));
    }
    public void setSuperscript_Off() {
        out.append(new String(new char[]{ 0x1b, 0x54 }));
    }
    
    public void setSubscript_On() {
        out.append(new String(new char[]{ 0x1b, 0x53, 1 }));
    }
    public void setSubscript_Off() {
        out.append(new String(new char[]{ 0x1b, 0x54 }));
    }

    public void setOutline_Off() {
        out.append(new String(new char[]{ 0x1b, 0x71, 0 }));
    }
    public void setOutline_On() {
        out.append(new String(new char[]{ 0x1b, 0x71, 1 }));
    }

    public void setShadow_Off() {
        out.append(new String(new char[]{ 0x1b, 0x71, 0 }));
    }
    public void setShadow_On() {
        out.append(new String(new char[]{ 0x1b, 0x71, 2 }));
    }

    public void setCondensed_On() {
        out.append(new String(new char[]{ 0x0f }));
    }
    public void setCondensed_Off() {
        out.append(new String(new char[]{ 0x12 }));
    }
    
    public final PrintModeImpl PrintMode = new PrintModeImpl();
    
    public class PrintModeImpl {
        
        public void setCharacterFontA_CPI_10() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setCharacterFontB_CPI_12() {
            out.append(new String(new char[]{ 0x1b, '!', 1 })); 
        }

        public void setProportional_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setProportional_On() {
            out.append(new String(new char[]{ 0x1b, '!', 2 })); 
        }

        public void setCondensed_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setCondensed_On() {
            out.append(new String(new char[]{ 0x1b, '!', 4 })); 
        }

        public void setBold_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setBold_On() {
            out.append(new String(new char[]{ 0x1b, '!', 8 })); 
        }
        
        public void setDoubleStrike_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setDoubleStrike_On() {
            out.append(new String(new char[]{ 0x1b, '!', 16 })); 
        }

        public void setDoubleWidth_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setDoubleWidth_On() {
            out.append(new String(new char[]{ 0x1b, '!', 32 })); 
        }

        public void setItalics_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setItalics_On() {
            out.append(new String(new char[]{ 0x1b, '!', 64 })); 
        }

        public void setUnderline_Off() {
            out.append(new String(new char[]{ 0x1b, '!', 0 })); 
        }
        public void setUnderline_On() {
            out.append(new String(new char[]{ 0x1b, '!', 128 })); 
        }
    }    
}
