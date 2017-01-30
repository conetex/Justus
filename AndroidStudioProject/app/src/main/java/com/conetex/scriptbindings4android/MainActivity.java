package com.conetex.scriptbindings4android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.liquidplayer.webkit.javascriptcore.JSContext;
import org.liquidplayer.webkit.javascriptcore.JSValue;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    public static class CallBack{
        public static void callback4c() {
            System.out.println("hi! we are in java... O");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

/* //////////////////////////////////////////////////////////////////////////////////////////////
        try {
            MyScriptable.main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/ //////////////////////////////////////////////////////////////////////////////////////////////

/* //////////////////////////////////////////////////////////////////////////////////////////////
        JSContext context = new JSContext();
        String script =
                "function factorial(x) { var f = 1; for(; x > 1; x--) f *= x; return f; }\n" +
                        "var fact_a = factorial(3);\n";
        JSValue jsValue = context.evaluateScript(script);
        String s = jsValue.toString();
        System.out.println(s);
*/ //////////////////////////////////////////////////////////////////////////////////////////////

///* ////////////////////////////////////////////////////////////////////////////////////////////// C
        // Every Rhino VM begins with the enter()
        // This Context is not Android's Context
        Context rhino = Context.enter();

        // Turn off optimization to make Rhino Android compatible
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();

            // Note the forth argument is 1, which means the JavaScript source has
            // been compressed to only one line using something like YUI
            //String classOfThis = "var import = com.conetex.scriptbindings4android.MainActivity.MyScriptable;";
            //String classOfThis = "var MyJavaClass = Java.type('com.conetex.scriptbindings4android.MainActivity.MyScriptable');";
            String javaScriptCode =
                    "var im = Packages.com.conetex.scriptbindings4android.MainActivity.CallBack; " +
                    "function factorial(x,c) { var f = 1; for(; x > 1; x--) f *= x; im.callback4c(); return f; }\n" //+
                    //"var fact_a = factorial(3);\n"
                    ;
                    //"function factorial(x,c) { var f = 1; for(; x > 1; x--) f *= x; return f; }\n" +
                    //        "var fact_a = factorial(3);\n";
            //rhino.evaluateString(scope, classOfThis + javaScriptCode, "JavaScript", 1, null);
            //rhino.evaluateString(scope, javaScriptCode, "JavaScript", 1, null);
            rhino.evaluateString(scope, javaScriptCode, "ScriptAPI", 1, null);

            // Get the functionName defined in JavaScriptCode
            Object obj = scope.get("factorial", scope);

            if (obj instanceof Function) {
                Function jsFunction = (Function) obj;

                // Call the function with params
                Object[] params = {new Integer(4), new CallBack()};
                Object jsResult = jsFunction.call(rhino, scope, scope, params);
                // Parse the jsResult object to a String
                String result = Context.toString(jsResult);
            }
        } finally {
            Context.exit();
        }
//*/ //////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
