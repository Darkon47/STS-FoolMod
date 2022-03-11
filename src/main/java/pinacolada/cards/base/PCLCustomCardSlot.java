package pinacolada.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.utilities.PCLJUtils;

import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PCLCustomCardSlot {
    public static final String BaseID = "pclCustom";
    private static final String Folder = "PCLCustomCards";
    private static final FilenameFilter Filter = (dir, name) -> name.endsWith(".json");
    private static final Gson GsonReader = new Gson();
    private static final TypeToken<PCLCustomCardSlot> TToken = new TypeToken<PCLCustomCardSlot>(){};
    private static final HashMap<AbstractCard.CardColor, ArrayList<PCLCustomCardSlot>> CustomCards = new HashMap<>();

    public String ID;
    public String Name;
    public String Series;
    public String Type;
    public String Rarity;
    public String Color;
    public String Target;
    public String AttackTarget;
    public String AttackType;
    public String AttackEffect;
    public String InternalPortrait;
    public String InternalImage;
    public String ExternalImage;
    public String Description;
    public String UpgradeDescription;
    public Integer Cost;
    public Integer CostUpgrade;
    public Float[] ImageColor;
    public Integer[] Numbers;
    public Integer[] NumbersUpgrades;
    public Integer[] Affinities;
    public Integer[] AffinitiesUpgrades;
    public Integer[] AffinitiesScaling;
    public Integer[] AffinitiesRequirement;
    public String[] Tags;
    public String[] TagsUpgrades;
    public String[] Effects;
    public String[] ExtraDescriptions;


    public boolean IsEnabled = true;
    public transient PCLCardBuilder Builder;
    public transient AbstractCard.CardColor SlotColor = AbstractCard.CardColor.COLORLESS;
    protected transient String filePath;

    public static void Initialize() {
        CustomCards.clear();
        FileHandle folder = GetFolder();
        for (FileHandle f : folder.list(Filter)) {
            String path = f.path();
            try {
                String jsonString = f.readString();
                PCLCustomCardSlot slot = GsonReader.fromJson(jsonString, TToken.getType());
                slot.SetupBuilder(path);
                GetCards(slot.SlotColor).add(slot);
            }
            catch (Exception e) {
                e.printStackTrace();
                PCLJUtils.LogError(PCLCustomCardSlot.class, "Could not load Custom Card: " + path);
            }
        }
    }

    public static PCLCustomCardSlot Get(String id) {
        for (ArrayList<PCLCustomCardSlot> slots : CustomCards.values()) {
            for (PCLCustomCardSlot slot : slots) {
                if (id.equals(slot.ID)) {
                    return slot;
                }
            }
        }
        return null;
    }

    public static ArrayList<PCLCustomCardSlot> GetCards(AbstractCard.CardColor color) {
        if (!CustomCards.containsKey(color)) {
            CustomCards.put(color, new ArrayList<>());
        }
        return CustomCards.get(color);
    }

    protected static FileHandle GetFolder() {
        FileHandle folder = Gdx.files.local(Folder);
        if (!folder.exists()) {
            folder.mkdirs();
            PCLJUtils.LogInfo(PCLCustomCardSlot.class, "Created Custom Card Folder: " + folder.path());
        }
        return folder;
    }

    protected static String MakeNewID(AbstractCard.CardColor color) {
        String newID = BaseID + "_" + color.name() + "_" + MathUtils.random(999999);
        if (PCLJUtils.Any(GetCards(color), c -> c.ID.equals(newID))) {
            return BaseID + "_" + color.name() + "_" + MathUtils.random(999999) + "_1";
        }
        return newID;
    }

    public PCLCustomCardSlot(AbstractCard.CardColor color) {
        Builder = new PCLCardBuilder(MakeNewID(color))
                .SetColor(color)
                .SetText("", "", "");
        Builder.isTempHP = true;
        Builder.isHeal = true;
        IsEnabled = true;
    }

    public void WipeBuilder() {
        FileHandle writer = Gdx.files.local(filePath);
        writer.delete();
        PCLJUtils.LogInfo(PCLCustomCardSlot.class, "Deleted Custom Card: " + filePath);
    }

    public void CommitBuilder() {
        ID = Builder.id;
        Name = Builder.cardStrings.NAME;
        Series = Builder.series.toString();
        Type = Builder.cardType.name();
        Rarity = Builder.cardRarity.name();
        Color = Builder.cardColor.name();
        Target = Builder.cardTarget.name();
        AttackTarget = Builder.attackTarget.name();
        AttackType = Builder.attackType.name();
        AttackEffect = Builder.attackEffect.name();

        ExternalImage = Builder.imagePath;
        Description = Builder.cardStrings.DESCRIPTION;
        UpgradeDescription = Builder.cardStrings.UPGRADE_DESCRIPTION;
        ExtraDescriptions = Builder.cardStrings.EXTENDED_DESCRIPTION;

        Cost = Builder.cost;
        CostUpgrade = Builder.costUpgrade;
        Numbers = new Integer[] {Builder.damage, Builder.block, Builder.magicNumber, Builder.secondaryValue, Builder.hitCount};
        NumbersUpgrades = new Integer[] {Builder.damageUpgrade, Builder.blockUpgrade, Builder.magicNumberUpgrade, Builder.secondaryValueUpgrade, Builder.hitCountUpgrade};
        Affinities = Builder.affinities.GetAffinityLevelsAsArray();
        AffinitiesUpgrades = Builder.affinities.GetAffinityUpgradesAsArray();
        AffinitiesScaling = Builder.affinities.GetAffinityScalingsAsArray();
        AffinitiesRequirement = Builder.affinities.GetAffinityRequirementsAsArray();
        Tags = PCLJUtils.Map(Builder.tags, Enum::name).toArray(new String[]{});
        Effects = PCLJUtils.Filter(PCLJUtils.Map(Builder.effects, b -> b != null ? b.Serialize() : null), Objects::nonNull).toArray(new String[]{});

        if (filePath == null) {
            filePath = Folder + "/" + ID + ".json";
        }

        FileHandle writer = Gdx.files.local(filePath);
        writer.writeString(GsonReader.toJson(this, TToken.getType()), false);
        PCLJUtils.LogInfo(PCLCustomCardSlot.class, "Saved Custom Card: " + filePath);
    }

    public void SetupBuilder(String p) {
        SlotColor = AbstractCard.CardColor.valueOf(Color);

        this.Builder = new PCLCardBuilder(ID);
        this.Builder.SetText(Name, Description, UpgradeDescription, ExtraDescriptions);
        this.Builder.SetProperties(AbstractCard.CardType.valueOf(Type), AbstractCard.CardRarity.valueOf(Rarity));
        this.Builder.SetCardTarget(PCLCardTarget.valueOf(AttackTarget));
        this.Builder.SetTags(PCLJUtils.Map(Tags, AbstractCard.CardTags::valueOf));
        this.Builder.SetColor(SlotColor);
        this.Builder.SetSeries(CardSeries.GetByName(Series, false));
        this.Builder.SetAttackType(PCLAttackType.valueOf(AttackType));
        this.Builder.SetAttackEffect(AbstractGameAction.AttackEffect.valueOf(AttackEffect));

        this.Builder.SetNumbers(Numbers[0], Numbers[1], Numbers[2], Numbers[3], Numbers[4]);
        this.Builder.SetUpgrades(NumbersUpgrades[0], NumbersUpgrades[1], NumbersUpgrades[2], NumbersUpgrades[3], NumbersUpgrades[4]);
        this.Builder.SetCost(Cost, CostUpgrade);
        this.Builder.SetAffinities(Affinities, AffinitiesUpgrades, AffinitiesScaling, AffinitiesRequirement);
        this.Builder.SetBaseEffect(PCLJUtils.Filter(PCLJUtils.Map(Effects, BaseEffect::Get), Objects::nonNull));

        this.Builder.SetImagePath(ExternalImage);

        this.Builder.isTempHP = true;
        this.Builder.isHeal = true;

        filePath = p;
        PCLJUtils.LogInfo(PCLCustomCardSlot.class, "Loaded Custom Card: " + filePath);
    }
}
