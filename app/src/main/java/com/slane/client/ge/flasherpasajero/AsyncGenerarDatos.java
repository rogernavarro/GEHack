package com.slane.client.ge.flasherpasajero;

/**
 * Created by Roger Navarro on 29/09/2018.
 */
public class AsyncGenerarDatos extends Thread
{
    public IAsyncEvent evento;
    int indice = 0;

    double[][] coordenadas={
            {20.664786, -100.400155},
            {20.666318, -100.399848},
            {20.661564, -100.387427},
            {20.662431, -100.376535},
            {20.659757, -100.352586},
            {20.660278, -100.347638},
            {20.657249, -100.349287},
            {20.656295, -100.349487},
            {20.656541, -100.348979},
            {20.660809, -100.346416},
            {20.657258, -100.343225},
            {20.656862, -100.343060},
            {20.660420, -100.343984},
            {20.662814, -100.377411},
            {20.663190, -100.391411},
            {20.666890, -100.403681},
            {20.667733, -100.404591},
            {20.667634, -100.405032},
            {20.666347, -100.401733},
            {20.664779, -100.400450}};

    public void run()
    {
        boolean bandera = true;
        while(bandera)
        {
            if(indice > 18)
            {
                bandera = false;
            }

            try {
                Thread.sleep(5000);
            }
            catch (Exception e){ }

            if ( (this.evento != null) && (!frmMapa.isrun) )
                this.evento.Event( String.valueOf(coordenadas[indice][0]), String.valueOf(coordenadas[indice][1]) );

            indice++;
        }
    }

}
