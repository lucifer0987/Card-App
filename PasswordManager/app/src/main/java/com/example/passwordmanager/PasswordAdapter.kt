package com.example.passwordmanager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.PasswordAdapter.*
import com.example.passwordmanager.models.password

class PasswordAdapter(private val context: Context) : RecyclerView.Adapter<PasswordHolder>() {

    companion object{
        var data = arrayListOf<password>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.password_item, parent, false)
        return PasswordHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordHolder, position: Int) {

        holder.service.text = data.get(position).service_name.toString()
        holder.email.text = data.get(position).service_email_phone.toString()

        holder.copy.setOnClickListener{
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("password", data.get(position).service_password)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Password has been copied to your clipboard", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class PasswordHolder(v: View): RecyclerView.ViewHolder(v) {
        val service:TextView = v.findViewById(R.id.service)
        val email:TextView = v.findViewById(R.id.email)
        val copy: Button = v.findViewById(R.id.btn_copy)
    }

    fun setData(par:ArrayList<password>){
        data = par
        notifyDataSetChanged()
    }

}