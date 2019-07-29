package br.com.ronanlima.twolevelmenu.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.ronanlima.twolevelmenu.model.Menu;

public class MenuUtil {

    public static String openRawResource(Context context, int menuResId) throws IOException {
        InputStream menu = context.getResources().openRawResource(menuResId);
        int length = menu.available();
        byte[] data = new byte[length];
        menu.read(data);
        return new String(data);
    }

    public static List<Menu> fromJsonArrayToList(String str) throws JSONException {
        JSONArray jsonArray = new JSONArray(str);
        Menu menu;
        List<Menu> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = (JSONObject) jsonArray.get(i);
            List<Menu> items = convertToList(item.getJSONArray("items").toString());
            menu = new Menu(item.getInt("idMenu"), item.getString("titulo"), items, item.getString("icone")
                    , item.getString("dataUltAtualizacao"), false, false, Menu.TipoMenu.MENU_PAI);
            list.add(menu);
        }
        return list;
    }

    public static <T> List<T> convertToList(String buffer) {
        try {
            JSONArray jsonArray = new JSONArray(buffer);
            Type listType = new TypeToken<List<Menu>>() {
            }.getType();
            return new Gson().fromJson(String.valueOf(jsonArray), listType);
        } catch (Exception n) {
            n.printStackTrace();
            return null;
        } catch (IllegalAccessError i) {
            i.printStackTrace();
            return null;
        }
    }

    public static void checkMenu(Menu menu, List<Menu> menus, RecyclerView.Adapter adapter, int position) {
        int count = 0;
        for (Menu m : menus) {
            if (m.isChecked()) {
                m.setChecked(false);
                adapter.notifyItemChanged(count);
            } else {
                m.setChecked(false);
            }
            count++;
        }
        menu.setChecked(true);
        adapter.notifyItemChanged(position);
    }

}
