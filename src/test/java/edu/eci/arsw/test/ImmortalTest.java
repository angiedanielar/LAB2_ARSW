package edu.eci.arsw.test;

import edu.eci.arsw.highlandersim.ControlFrame;
import edu.eci.arsw.highlandersim.Immortal;
import org.junit.Test;
import static org.junit.Assert.*;

public class ImmortalTest {

    private ControlFrame immortalGame;

    public ImmortalTest(){}

    @Test
    public void deberiaDetenerseElJuego() {
        try{
            immortalGame = new ControlFrame();
            immortalGame.start();
            immortalGame.detener();
            boolean condition = immortalGame.isStop();
            assertTrue(condition);
        }catch (Exception e){
            fail("Lanzo excepción");
        }
    }

    @Test
    public void deberiaNoEstarDetenidoElJuegoAlComenzar() {
        try{
            immortalGame = new ControlFrame();
            immortalGame.start();
            boolean condition = immortalGame.isStop();
            assertFalse(condition);
        }catch (Exception e){
            fail("Lanzo excepción");
        }
    }


    @Test
    public void deberiaCumplirseElInvarianteAlPausar() {
        try {
            immortalGame = new ControlFrame();
            immortalGame.start();
            immortalGame.pause();
            int cont = 0;
            for(Immortal i: immortalGame.getImmortals()){
                cont += i.getHealth();
            }

            assertEquals(immortalGame.getNumOfImmortals() * immortalGame.getDefaultImmortalHealth(), cont);

        }

        catch (Exception e) {
            fail("Lanzó excepción");
        }
    }

        @Test
        public void deberiaCumplirseElInvarianteAlReiniciar() {
            try {
                immortalGame = new ControlFrame();
                immortalGame.start();
                Thread.sleep(500);
                immortalGame.pause();
                immortalGame.reiniciar();
                Thread.sleep(500);
                immortalGame.pause();
                int cont = 0;
                for(Immortal im: immortalGame.getImmortals()){
                    cont += im.getHealth();
                }

                assertEquals(immortalGame.getNumOfImmortals() * immortalGame.getDefaultImmortalHealth(), cont);

            }
            
            catch (Exception e){
                fail("Lanzó excepción");
            }
        }

    @Test
    public  void deberianVolverNuevosJugadoresAlReiniciar(){
        try{
            immortalGame = new ControlFrame();
            immortalGame.start();
            immortalGame.detener();
            Immortal immortal0 = immortalGame.getImmortals().get(0);
            immortalGame.start();
            immortalGame.detener();
            assertFalse(immortalGame.getImmortals().contains(immortal0));
        }

        catch (Exception e){
            fail("Lanzo excepción");
        }
    }

}
