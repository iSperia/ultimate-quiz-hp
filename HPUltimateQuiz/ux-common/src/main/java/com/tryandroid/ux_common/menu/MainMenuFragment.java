package com.tryandroid.ux_common.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tryandroid.ux_common.R;
import com.tryandroid.ux_common.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuFragment extends Fragment implements MenuView {

    @BindView(R2.id.menu_items_layout)
    LinearLayout menuItemsLayout;

    private List<View> menuItemViews = new ArrayList<>();

    private MenuPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, result);

        presenter = ((Providers) getActivity()).provideMenuPresenter(this);
        return result;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            presenter.start();
        }
    }

    @Override
    public void onStop() {
        presenter.hibernate();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.save(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            presenter.restore(savedInstanceState);
        }
    }

    @Override
    public void showMenu(List<String> menuPoints) {
        final LayoutInflater inflater = getLayoutInflater();
        disposeMenu();
        int idx = 0;
        for (final String menuText : menuPoints) {
            final View menuItem = inflater.inflate(R.layout.layout_menu_item, menuItemsLayout);
            final Button button = menuItem.findViewById(R.id.item_button);
            button.setText(menuText);
            button.setTag(idx);
            button.setOnClickListener(this::handleMenuItem);
            menuItemViews.add(menuItem);
        }
    }

    private void handleMenuItem(final View view) {
        final Integer index = (Integer) view.getTag();
        presenter.onMenuItemClicked(index);
    }

    private void disposeMenu() {
        for (final View view : menuItemViews) {
            view.setOnClickListener(null);
        }
        menuItemsLayout.removeAllViews();
        menuItemViews.clear();
    }



    public interface Providers {

        MenuPresenter provideMenuPresenter(final MenuView view);
    }
}
