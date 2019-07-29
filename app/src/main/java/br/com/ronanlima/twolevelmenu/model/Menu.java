package br.com.ronanlima.twolevelmenu.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.ronanlima.twolevelmenu.R;
import br.com.ronanlima.twolevelmenu.interfaces.MenuItemClickListener;
import br.com.ronanlima.twolevelmenu.util.DataUtil;

@Entity(tableName = "moreUsedMenu", primaryKeys = {"userId", "idMenu"})
public class Menu implements Parcelable {
    public static final int ID_MAIS_UTILIZADOS = -1;

    @NonNull
    private String userId;
    @NonNull
    private Integer idMenu;
    @NonNull
    private String titulo;
    @Ignore
    private List<Menu> items;
    @Ignore
    private String icone;
    @NonNull
    private String link;
    @Ignore
    private String dataUltAtualizacao;
    @NonNull
    private Integer qtdClick;
    @Ignore
    private boolean checked = false;
    @Ignore
    private boolean isShowBadgeUpdate = false;
    @Ignore
    private TipoMenu tipoMenu;
    private Date dataVisualizacao;

    @Ignore
    public Menu() {
    }

    @Ignore
    public Menu(String titulo, TipoMenu tipoMenu) {
        setTitulo(titulo);
        setItems(new ArrayList<Menu>());
        setTipoMenu(tipoMenu);
    }

    public Menu(@NonNull String userId, @NonNull Integer idMenu, @NonNull String titulo, @NonNull String link, @NonNull Integer qtdClick, Date dataVisualizacao) {
        setUserId(userId);
        setIdMenu(idMenu);
        setTitulo(titulo);
        setLink(link);
        setQtdClick(qtdClick);
        setDataVisualizacao(dataVisualizacao);
    }

    @Ignore
    public Menu(Integer idMenu, String titulo, List<Menu> items, TipoMenu tipoMenu) {
        setIdMenu(idMenu);
        setTitulo(titulo);
        setItems(items);
        setTipoMenu(tipoMenu);
    }

    @Ignore
    public Menu(Integer idMenu, String titulo, List<Menu> items, String icone, String dataUltAtualizacao, boolean checked, boolean isShowBadgeUpdate, TipoMenu tipoMenu) {
        setIdMenu(idMenu);
        setTitulo(titulo);
        setItems(items);
        setIcone(icone);
        setDataUltAtualizacao(dataUltAtualizacao);
        setChecked(checked);
        setShowBadgeUpdate(isShowBadgeUpdate);
        setTipoMenu(tipoMenu);
    }

    @Ignore
    public Menu(Integer idMenu, String titulo, List<Menu> items, String icone, String dataUltAtualizacao, boolean checked, boolean isShowBadgeUpdate, String tipoMenu) {
        setIdMenu(idMenu);
        setTitulo(titulo);
        setItems(items);
        setIcone(icone);
        setDataUltAtualizacao(dataUltAtualizacao);
        setChecked(checked);
        setShowBadgeUpdate(isShowBadgeUpdate);
        setTipoMenu(TipoMenu.getByName(tipoMenu));
    }

    @Ignore
    protected Menu(Parcel in) {
        titulo = in.readString();
        items = in.readParcelable(Menu.class.getClassLoader());
        icone = in.readString();
        dataUltAtualizacao = in.readString();
        checked = in.readByte() != 0;
        isShowBadgeUpdate = in.readByte() != 0;
    }

    public enum TipoMenu {
        MENU_PAI(1) {
            @Override
            public void click(MenuItemClickListener menuItemClickListener, Menu menu) {
                if (menu.getItems() != null && !menu.getItems().isEmpty()) {
                    menuItemClickListener.onMenuClick(menu);
                } else {
                    menuItemClickListener.onItemMenuClick(menu);
                }
            }
        }, MENU_FILHO(2) {
            @Override
            public void click(MenuItemClickListener menuItemClickListener, Menu menu) {
                if (menu.getLink() != null && !menu.getLink().equals("")) {
                    menuItemClickListener.onItemMenuClick(menu);
                }
            }
        }, MENU_MORTO(3) {
            @Override
            public void click(MenuItemClickListener menuItemClickListener, Menu menu) {
                Log.i("TipoMenu", "MENU_MORTO");
            }
        };

        private int index;

        TipoMenu(int index) {
            this.index = index;
        }

        public static TipoMenu getByName(String name) {
            for (TipoMenu m : values()) {
                if (name.equals(m.name())) {
                    return m;
                }
            }
            return null;
        }

        public abstract void click(MenuItemClickListener menuItemClickListener, Menu menu);
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    @NonNull
    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(@NonNull Integer idMenu) {
        this.idMenu = idMenu;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NonNull String titulo) {
        this.titulo = titulo;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    @NonNull
    public String getLink() {
        return link;
    }

    public void setLink(@NonNull String link) {
        this.link = link;
    }

    public String getDataUltAtualizacao() {
        return dataUltAtualizacao;
    }

    public void setDataUltAtualizacao(String dataUltAtualizacao) {
        this.dataUltAtualizacao = dataUltAtualizacao;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public Integer getQtdClick() {
        return qtdClick;
    }

    public void setQtdClick(@NonNull Integer qtdClick) {
        this.qtdClick = qtdClick;
    }

    public TipoMenu getTipoMenu() {
        return tipoMenu;
    }

    public void setTipoMenu(TipoMenu tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    public Date getDataVisualizacao() {
        return dataVisualizacao;
    }

    public void setDataVisualizacao(Date dataVisualizacao) {
        this.dataVisualizacao = dataVisualizacao;
    }

    public boolean isShowBadgeUpdate(Menu menu) {
        if (menu.getDataUltAtualizacao() != null && !menu.getDataUltAtualizacao().trim().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            Date date = DataUtil.transformaData(menu.getDataUltAtualizacao());
            if (cal.getTime().compareTo(date) >= 0) {
                cal.add(Calendar.DAY_OF_MONTH, -30);
                boolean isDataValida = date.after(cal.getTime());
                if (isDataValida && menu.getDataVisualizacao() == null) {
                    return true;
                } else if (isDataValida && date.after(menu.getDataVisualizacao())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isParentShowBadgeUpdate() {
        if (items != null) {
            for (Menu m : items) {
                if (m.isShowBadgeUpdate(m)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getColorBackground() {
        if (isChecked()) {
            return R.color.menuSelecionado;
        }
        return R.color.backgroundMenu;
    }

    public void setShowBadgeUpdate(boolean showBadgeUpdate) {
        isShowBadgeUpdate = showBadgeUpdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeTypedList(items);
        dest.writeString(icone);
        dest.writeString(dataUltAtualizacao);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (isShowBadgeUpdate ? 1 : 0));
    }
}
