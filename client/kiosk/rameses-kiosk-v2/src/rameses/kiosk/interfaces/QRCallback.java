/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rameses.kiosk.interfaces;

/**
 *
 * @author louie
 */
public interface QRCallback {
    public void process( String qrcode );
    public void show();
    public void hide();
    public void back();
    public void backToMain();
}
