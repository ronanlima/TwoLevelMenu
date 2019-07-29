package br.com.ronanlima.twolevelmenu.interfaces;

import br.com.ronanlima.twolevelmenu.model.Menu;

public interface MenuItemClickListener {
    void onMenuClick(Menu menu);

    void onItemMenuClick(Menu menu);
}
