package com.Bolquri4.Xehvus.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.Bolquri4.Xehvus.R
import com.Bolquri4.Xehvus.core.theme.SurfaceWarm
import com.Bolquri4.Xehvus.core.theme.TextPrimary
import com.Bolquri4.Xehvus.core.theme.TextSecondary
import com.Bolquri4.Xehvus.core.ui.DangerButton
import com.Bolquri4.Xehvus.core.ui.PrimaryButton

@Composable
fun ConfirmResetDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
    ) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = SurfaceWarm,
            shadowElevation = 16.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Text(
                    text = stringResource(R.string.reset_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                PrimaryButton(
                    text = stringResource(R.string.cta_cancel),
                    onClick = onCancel,
                    height = 56.dp
                )
                DangerButton(
                    text = stringResource(R.string.cta_reset_confirm),
                    onClick = onConfirm
                )
            }
        }
    }
}
