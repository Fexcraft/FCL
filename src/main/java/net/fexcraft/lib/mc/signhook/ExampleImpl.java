package net.fexcraft.lib.mc.signhook;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.network.Network;
import net.fexcraft.lib.mc.utils.Formatter;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExampleImpl extends SignInteractionHook {

	public static final Identifier ID = new Identifier("fcl:update_checker");
	private long lastcheck;
	
	public ExampleImpl(CompoundTag compound){
		super(compound); this.readNBT(compound);

	}
	public ExampleImpl(SignBlockEntity ent){
		super(ent);
		ent.text[0] = new LiteralText(Formatter.format("&0[&9FCL-UC&0]"));
		ent.text[1] = new LiteralText(ent.text[1].asString().toLowerCase());
		lastcheck = Time.getDate();
		ent.text[3] = new LiteralText(Time.getAsString(Time.getDate()));
		String ver = getLatestVersion(ent.text[1].asString());
		if(ver == null || ver.equals("null")){ ver = "&6not found"; }
		ent.text[2] = new LiteralText(Formatter.format(ver));
		((SignEntityHook)ent).sendSIHUpdate();
	}

	private String getLatestVersion(String modid){
		JsonObject obj = Network.getModData(modid);
		if(obj == null || obj.toString().equals("{}")){
			return null;
		}
		if(obj.has("versions")){
			for(JsonElement elm : obj.get("versions").getAsJsonArray()){
				if(elm.getAsJsonObject().get("version").getAsString().equals(FCL.mcv)){
					return "&2" + elm.getAsJsonObject().get("latest_version").getAsString();
				}
			}
			return "&cno version data";
		}
		else if(obj.has("latest_version")){
			return "&2" + obj.get("latest_version").getAsString();
		}
		return "&cno version data";
	}

	@Override
	public boolean onBlockActivate(SignBlockEntity entity, BlockState state, World world, BlockPos pos, PlayerEntity ent, Hand hand, BlockHitResult result){
		if(lastcheck + Time.HOUR_MS + Time.HOUR_MS < Time.getDate()){
			String ver = getLatestVersion(entity.text[1].asString());
			if(ver == null || ver.equals("null")){ ver = "&6not found"; }
			entity.text[2] = new LiteralText(Formatter.format(ver).toLowerCase());
			lastcheck = Time.getDate(); ((SignEntityHook)ent).sendSIHUpdate();
			return true;
		}
		else{
			Print.chat(ent, "Last check was less than 2 hours ago.");
			return true;
		}
	}

	@Override
	public Identifier getID(){
		return ID;
	}

	@Override
	public String getKeyword(){
		return "[fc-uc]";
	}

	@Override
	public void readNBT(CompoundTag tag){
		lastcheck = tag.getLong("uc-lastcheck");
	}

	@Override
	public void writeNBT(CompoundTag tag){
		tag.putLong("uc-lastcheck", lastcheck);
	}

}
