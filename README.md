# TwoLevelMenu

This project was built to attend those developers who's need a two-level menu.

# Images

Following, the images off menu generated
![](/images/Screenshot_1.png)
![](/images/Screenshot_2.png)
![](/images/Screenshot_3.png)
![](/images/Screenshot_4.png)

# Bonus
In this project, the load of menu was made by an JSON on the /raw directory. If you need to load on response of a webservice, just follow the code below:
```
if (response.isSuccessful()) {
    menus.clear();
    menus.addAll(response.body());
    if (moreUsedMenus != null && !moreUsedMenus.isEmpty()) {
        insertMoreUsedMenus();
        int indexMenu = 0;
        for (Menu mU : moreUsedMenus) {
            try {
                iconNewExibitionControl(indexMenu, mU);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("DisplayList", e.getMessage());
            }
            indexMenu = 0;
        }
    }
    menuAdapter.setMenus(menus);
    itemMenuAdapter.updateMenuFromServer(menus);
    if (search != null) {
        search.setText("");
    }
} else {
    sendDefaultRetrofitException(response);
}
```

# Prototype
Made by [Paulo Washington](https://www.behance.net/paulowashington)

Enjoy! :)
