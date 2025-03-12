package cc.atomtech.timetable.views.management

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cc.atomtech.timetable.AppVersion
import cc.atomtech.timetable.StringRes
import cc.atomtech.timetable.models.utilities.WhatsNewParagraph
import cc.atomtech.timetables.resources.Res
import cc.atomtech.timetables.resources.appicon
import cc.atomtech.timetables.resources.whatsnew
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource

@Composable
fun WhatsNew() {
    val whatsNewContent = Json.decodeFromString<List<WhatsNewParagraph>>(StringRes.get("whatsnew"))

    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( end = 12.dp, top = 8.dp, bottom = 12.dp )
            ) {
                val uriHandler = LocalUriHandler.current

                Box {
                    Image(
                        painter = painterResource(Res.drawable.whatsnew),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .rotate(180F)
                            .height(192.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        content = {
                            Icon(
                                Icons.Rounded.Info,
                                contentDescription = "Info",
                                tint = Color.White,
                                modifier = Modifier.background(
                                    androidx.compose.material3.MaterialTheme.colorScheme.background,
                                    shape = CircleShape
                                )
                            )
                        },
                        onClick = {
                            uriHandler.openUri(StringRes.get("version_name_info"))
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(Res.drawable.appicon),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .size(72.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = 16.dp, y = 32.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer( modifier = Modifier.height( 48.dp ) )
                Text(
                    text = "${StringRes.get("app_name")} ${AppVersion.versionName}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "\"${StringRes.get("version_name")}\" (${AppVersion.versionCode})",
                    fontSize = 12.sp
                )
            }
        }
        items(whatsNewContent) {
            Text(
                it.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                it.content.joinToString("\n\n")
            )
            Spacer( modifier = Modifier.height(12.dp) )
        }
    }
}
