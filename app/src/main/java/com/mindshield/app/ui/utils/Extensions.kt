package com.mindshield.app.ui.utils

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Applies padding using the theme's default spacing if the padding is not specified.
 */
fun Modifier.defaultPadding(
    start: Dp = Dimens.paddingMedium,
    top: Dp = Dimens.paddingMedium,
    end: Dp = Dimens.paddingMedium,
    bottom: Dp = Dimens.paddingMedium
) = this.padding(start, top, end, bottom)

/**
 * Applies horizontal padding using the theme's default spacing.
 */
fun Modifier.defaultHorizontalPadding(horizontal: Dp = Dimens.paddingMedium) =
    this.padding(horizontal = horizontal)

/**
 * Applies vertical padding using the theme's default spacing.
 */
fun Modifier.defaultVerticalPadding(vertical: Dp = Dimens.paddingMedium) =
    this.padding(vertical = vertical)

/**
 * A composable that provides a preview container with default padding.
 */
@Composable
fun PreviewContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .defaultPadding(),
        content = { content() }
    )
}
