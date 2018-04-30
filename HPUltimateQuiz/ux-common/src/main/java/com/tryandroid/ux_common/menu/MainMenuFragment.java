package com.tryandroid.ux_common.menu;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tryandroid.ux_common.BackKeyInterceptor;
import com.tryandroid.ux_common.R;
import com.tryandroid.ux_common.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuFragment extends Fragment implements BackKeyInterceptor {

    private static final String ARG_VIEW_MODEL_CLASS = "vm_class";

    @BindView(R2.id.menu_items_layout)
    LinearLayout menuItemsLayout;

    private List<View> menuItemViews = new ArrayList<>();

    private MenuViewModel menuViewModel;

    public static MainMenuFragment instantiate(final Class viewModel) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_VIEW_MODEL_CLASS, viewModel);
        final MainMenuFragment result = new MainMenuFragment();
        result.setArguments(args);
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View result = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, result);

        final Class<? extends MenuViewModel> viewModelClass =
                (Class) getArguments().getSerializable(ARG_VIEW_MODEL_CLASS);
        menuViewModel = ViewModelProviders.of(this).get(viewModelClass);

        return result;
    }

    private void showMenuItems(final List<String> menuItems) {
        final LayoutInflater inflater = getLayoutInflater();
        disposeMenu();
        int idx = 0;
        for (final String menuText : menuItems) {
            final View menuItem = inflater.inflate(R.layout.layout_menu_item, menuItemsLayout);
            final Button button = menuItem.findViewById(R.id.item_button);
            button.setText(menuText);
            button.setTag(idx);
            button.setOnClickListener(this::handleMenuItem);
            menuItemViews.add(menuItem);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuViewModel.observeMenuItems().observe(this, this::showMenuItems);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        menuViewModel.persist(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        menuViewModel.restore(savedInstanceState);
    }

    private void handleMenuItem(final View view) {
        menuViewModel.handleMenuItemClick((Integer) view.getTag());
    }

    private void disposeMenu() {
        for (final View view : menuItemViews) {
            view.setOnClickListener(null);
        }
        menuItemsLayout.removeAllViews();
        menuItemViews.clear();
    }

    @Override
    public boolean shouldInterceptBackKey() {
        return menuViewModel.shouldInterceptBackKey();
    }
}
