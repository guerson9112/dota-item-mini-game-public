package modelos;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;


public class Traslador {
    static int[] rotaciones = {-40, -30, -20, -10, 10, 20, 30, 40};
    static Juego juegoReferecia;
    static AnimationSet[] trasladores = null;
    static AnimationSet[] entrarCartaAnimations = null;


    public static AnimationSet[] getTrasladores(){
        if(trasladores == null){
            float altura = -0.58f;
            trasladores = new AnimationSet[rotaciones.length];

            for(int i = 0 ; i < trasladores.length ; i++){
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setDuration(250);
                animationSet.setStartOffset(0);
                animationSet.setFillAfter(true);

                RotateAnimation rotateAnimation = new RotateAnimation(
                        0, rotaciones[i],
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                TranslateAnimation animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, altura);

                animationSet.addAnimation(rotateAnimation);



                animationSet.addAnimation(animation);

                trasladores[i] = animationSet;
            }

        }

        return trasladores;
    }

    public static AnimationSet[] getEntrarCartaAnimation(final Juego juego){
        if(entrarCartaAnimations == null || juegoReferecia == null || juego != juegoReferecia){
            juegoReferecia = juego;
            entrarCartaAnimations = new AnimationSet[8];
            int startOffset = 0;

            for(int i = 0 ; i < entrarCartaAnimations.length ; i++){
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setDuration(250);
                animationSet.setStartOffset(startOffset);
                animationSet.setFillAfter(true);

                RotateAnimation rotateAnimation = new RotateAnimation(
                        360 , 0,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);

                TranslateAnimation animation = new TranslateAnimation(
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, -1,
                        Animation.RELATIVE_TO_PARENT, 0);

                animationSet.addAnimation(rotateAnimation);
                animationSet.addAnimation(animation);

                entrarCartaAnimations[i] = animationSet;
                startOffset += 40; // cada carta entra despues de 40 milisegundos despues de la anterior
                if(i == entrarCartaAnimations.length - 1){
                    entrarCartaAnimations[i].setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            juego.setPuedoHacerClick(true);
                            //Toast.makeText(juego.activity, "click si", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        }

        return entrarCartaAnimations;
    }
}
