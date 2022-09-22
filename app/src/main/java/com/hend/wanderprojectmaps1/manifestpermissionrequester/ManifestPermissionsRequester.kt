package com.hend.wanderprojectmaps1.manifestpermissionrequester

import android.app.Activity

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.hend.wanderprojectmaps1.Constant.Companion.REGISTRY_KEY

class ManifestPermissionsRequester(
    registry: ActivityResultRegistry,
    private val context: Activity
) : IManifestPermissionsRequester {
    private lateinit var permissionsResult: MutableList<Boolean>
    private lateinit var permissionsRequest: MutableList<String>
    private lateinit var permissions: Array<String>
    override var getPermission = registry.register(
        REGISTRY_KEY,
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        for (index in permissionsResult.indices) {
            permissionsResult[index] = permission[permissions[index]] ?: permissionsResult[index]
            Log.i("testhere", "$permission")
        }

    }

//    fun ActivityResultRegistry.getPerms() {
//        this.register(
//            REGISTRY_KEY,
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permission ->
//            for (index in permissionsResult.indices) {
//                permissionsResult[index] =
//                    permission[permissions[index]] ?: permissionsResult[index]
//                Log.i("testhere", "$permission")
//            }
//        }
//    }


    override fun requestPermission(permissions: Array<String>): MutableList<Boolean> {
        Log.i("test", "1")
        permissionsResult = ArrayList()
        permissionsRequest = ArrayList()
        this.permissions = permissions
        for (i in this.permissions) {
            permissionsResult.add(false)
        }
        for (permission in this.permissions.indices) {
            permissionsResult[permission] =
                ContextCompat.checkSelfPermission(context, this.permissions[permission]) ==
                        PackageManager.PERMISSION_GRANTED

        }
        for (index in permissionsResult.indices) {
            if (!permissionsResult[index]) {
                permissionsRequest.add(this.permissions[index])
            }
        }
        getPermission.launch(permissionsRequest.toTypedArray())
        Log.i("test", "$permissionsResult")
        Log.i("test", "$permissionsRequest")
        return permissionsResult
    }
}


