package com.example.ecomadminbatch04.models

import com.example.ecomadminbatch04.R

data class DashboardItem(
    val icon: Int,
    val title: String,
    val type: DashboardItemType
)

enum class DashboardItemType {
    ADD_PRODUCT, VIEW_PRODUCT, ORDER, CATEGORY, REPORT, USER, SETTINGS
}

val dashboardItemList = listOf<DashboardItem>(
    DashboardItem(icon = R.drawable.ic_baseline_add_24, title = "Add Product", type = DashboardItemType.ADD_PRODUCT),
    DashboardItem(icon = R.drawable.ic_baseline_list_24, title = "All Products", type = DashboardItemType.VIEW_PRODUCT),
    DashboardItem(icon = R.drawable.ic_baseline_monetization_on_24, title = "Order", type = DashboardItemType.ORDER),
    DashboardItem(icon = R.drawable.ic_baseline_category_24, title = "Category", type = DashboardItemType.CATEGORY),
    DashboardItem(icon = R.drawable.ic_baseline_insert_chart_24, title = "Report", type = DashboardItemType.REPORT),
    DashboardItem(icon = R.drawable.ic_baseline_supervised_user_circle_24, title = "Manager Users", type = DashboardItemType.USER),
    DashboardItem(icon = R.drawable.ic_baseline_settings_applications_24, title = "Settings", type = DashboardItemType.SETTINGS),
)
