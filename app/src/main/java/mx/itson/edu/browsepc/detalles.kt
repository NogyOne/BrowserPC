package mx.itson.edu.browsepc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class detalles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_detalles)

        val iv_producto: ImageView = findViewById(R.id.iv_producto)
        val tv_nombre: TextView = findViewById(R.id.tv_nombre)
        val tv_stock: TextView = findViewById(R.id.tv_stock)
        val tv_precio: TextView = findViewById(R.id.tv_precio)

        val bundle = intent.extras

        if(bundle != null){
            iv_producto.setImageResource(bundle.getInt("image"))
            tv_nombre.setText(bundle.getString("nombre"))
            tv_stock.setText(bundle.getString("stock"))
            tv_precio.setText(bundle.getString("precio"))
        }
    }
}