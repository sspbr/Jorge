package com.example.pesquisaandroid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softjads.adapter.modulo;
import com.softjads.service.DB;
import com.softjads.service.DataBase;
import com.softjads.sql.sql_create;
import com.softjads.sql.sql_select;

public class automatico extends Activity{
	private LinearLayout ll;
	private ArrayList<ImageButton> ImageButtons;
	private ArrayList<EditText> EditTexts;
	private TextView nTotalPes;
	
  	private SQLiteDatabase bd;
  	private Context context;
	private DataBase nDataBase;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.automatico);
	        
	       
	        
	        nDataBase = new DataBase(this);
			bd = nDataBase.getReadableDatabase();
			nDataBase.onCreate(bd);
			
			DB.getInstance(this);
			
		 this.setTitle(pesquisaSelecionada());
			
			ImageButtons = new ArrayList<ImageButton>();
			ll = (LinearLayout) findViewById(R.id.edits_ll);
			
			criarAluno();
			
			ImageButton btn_create = (ImageButton) findViewById(R.id.imageButton1);
			
			//Cursor cursor = bd.rawQuery(sql_select.GET_CONFIGURACAO,null);
			//cursor.moveToFirst();
			
			if (modulo.filtro_automatico.equals("1")){
				btn_create.setEnabled(false);
				btn_create.setImageResource(R.drawable.user);
			}
			else
			{
				btn_create.setEnabled(true);
			}
			
			
			
			btn_create.setOnClickListener(new View.OnClickListener() {
	        	@Override
	        	public void onClick(View arg0) {
	        		  insereRegistro();
	        		  ll.removeAllViewsInLayout();
	        		  criarAluno();
	        	}
	        });
			
		//	ImageButton btn_excluir = (ImageButton) findViewById(R.id.imageButton2);
		//	btn_excluir.setOnClickListener(new View.OnClickListener() {
	    //    	@Override
	    //    	public void onClick(View arg0) {
	        		
	    //    	}
	    //    });
	        
	    	
	        ImageButton volumemuteImageButton3 = (ImageButton) findViewById(R.id.imageButton1);
	        volumemuteImageButton3.setOnTouchListener(new View.OnTouchListener() 
	        { 
	        	public boolean onTouch(View v, MotionEvent event) { switch (event.getAction()) { 
	        	case MotionEvent.ACTION_DOWN: {  
	        		((ImageButton) v).setImageResource(R.drawable.user_mais_02);
	        		v.invalidate(); 
	        		break;
	        	}
	        	case MotionEvent.ACTION_UP: 
	        	{ 
	        		((ImageButton) v).setImageResource(R.drawable.user_mais_01);
	        		v.invalidate(); 
	        		break; 
	        	} 
	        	} 
	        		return false; 
	        	}
	        });
	    			
	    	
	    			
	    }
	 

	 
	 private void criarAluno(){
		   nDataBase = new DataBase(this);
		   bd = nDataBase.getReadableDatabase();
		   int quantidadePes = 0;
		   
		   
		   nTotalPes = (TextView) findViewById(R.id.editTextTotal);
		   nTotalPes.setText("Nenhum coletado!");
		   
		   String SQL_Completo = sql_select.GET_ALUNO;
		   
		   if (modulo.filtro_escola != 0 ){
			   SQL_Completo = SQL_Completo + " AND P.ID_ESCOLA = " + Integer.toString(modulo.filtro_escola);
		   }
		   if (modulo.filtro_turma != 0 ){
			   SQL_Completo = SQL_Completo + " AND P.ID_TURMA = " + Integer.toString(modulo.filtro_turma);
		   }  
		   if (!modulo.filtro_entrevistado.trim().equals("")){
			   SQL_Completo = SQL_Completo + " AND P.NOME LIKE '%" + (modulo.filtro_entrevistado) + "%' ";
		   }
			
		   SQL_Completo = SQL_Completo + "  ORDER BY P.ID_ALUNO DESC ";
		   
		   Cursor cursor = bd.rawQuery(SQL_Completo,null);
		   cursor.moveToFirst();
		   
		   try{

			int igual = 0;
			
			int f;
			
			while(!cursor.isAfterLast() )
			{
				igual = cursor.getInt(0) ;
				//count++;
				//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(   LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  80, 80);
				
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setOrientation(LinearLayout.VERTICAL);
				
				LinearLayout.LayoutParams paramsEdit = new LinearLayout.LayoutParams(  80, 80);
				
				ImageButton nImageButton = new ImageButton(this);
				 
				EditText nEditText = new EditText(this);
				nEditText.setText(cursor.getString(0) + " - " + cursor.getString(1));
				nEditText.setLeft(250);
				//nEditText.setEnabled(false);
				
				nEditText.setCursorVisible(false);
				nEditText.setFocusable(false);
				nEditText.setFocusableInTouchMode(false);
		        //android:clickable="false"

	
				nEditText.setTag(cursor.getString(0));
				nImageButton.setTag(cursor.getString(0));
				
				if (cursor.getInt(2) == 0){
					nImageButton.setImageResource(R.drawable.user);
						
				}else{
					nImageButton.setImageResource(R.drawable.user_ok);
					quantidadePes = quantidadePes + 1;
					
					if (quantidadePes == 1){
						nTotalPes.setText(Integer.toString(quantidadePes) +" foi coletado");
					}else
					{
						nTotalPes.setText(Integer.toString(quantidadePes) +" foram coletados");
					}
				}
					
				
				nImageButton.setBackgroundDrawable(null);
				nImageButton.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	String f = ((ImageButton) view).getTag().toString();
		            	modulo.AlunoAtual = Integer.parseInt(((ImageButton) view).getTag().toString());
		                Intent WSActivity = new Intent(automatico.this, confirmar.class);
		                startActivity(WSActivity);	
					      

		            }
		        });
				
				
				nEditText.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	modulo.AlunoAtual = Integer.parseInt(((EditText) view).getTag().toString());
		                Intent WSActivity = new Intent(automatico.this, confirmar.class);
		                startActivity(WSActivity);	
					
		            }
		        });
																
				ImageButtons.add(nImageButton); // adiciona a nova editText a lista.
				
				f = ll.getId();
				ll.addView(nImageButton); // adiciona a editText ao ViewGroup
				f = ll.getId();
				ll.addView(nEditText);
				
				cursor.moveToNext();
			}
		    }
		     finally{
		    	 cursor.close();
		    }
	 }
	 
     private void onInsert(Context context, ContentValues obj, String nTabela) { 
     	  try{
     		      		  
     		  DB.getInstance( context ).insert( nTabela, obj ); 
     	  }
     	  catch (Throwable ex){
     		  
     	  }
     	  
     }  
     
     public void insereRegistro(){
    	 try{
    	 int nMaxAluno = 0;
    	 Cursor cursor = bd.rawQuery(sql_select.GET_ALUNO_MAX,null);
    	 cursor.moveToFirst();
    	 try{
    	 
    	 if (cursor.getCount() > 0) {
    		 nMaxAluno = cursor.getInt(0);
    		 nMaxAluno = nMaxAluno + 1;
    	 }
    	 
     	ContentValues obj = new ContentValues(); 
 		obj.put("ID_ALUNO",nMaxAluno);
 		obj.put("ID_ESCOLA", "1");
 		obj.put("ID_TURMA","1");
 		obj.put("NOME","Entrevistado");
 		obj.put("DT_NASC","");
 		obj.put("SEXO","");
        this.onInsert( context, obj , sql_create.TABLE_ALUNO);  
    	 }
    	 finally{
    		 cursor.close();
    	 }
    	  }
    	  catch (Throwable ex){
    		  
    	  }
     }
     private String pesquisaSelecionada(){
    	 Cursor cursor2 = bd.rawQuery(sql_select.GET_CONFIGURACAO,null);
    	 cursor2.moveToFirst();
    	try{
    		if (cursor2.getCount() > 0) {
    			return "Pesquisa ativa é a: " + cursor2.getString(2).toString();
    		}else{
    			return "Nenhuma pesquisa Ativa!";
    		}
    	}
    	finally{
    		cursor2.close();
    		
    	}
     }
     
     @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
		  ll.removeAllViewsInLayout();
		  criarAluno();
    }
     
}

