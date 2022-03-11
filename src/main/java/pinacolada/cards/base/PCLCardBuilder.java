package pinacolada.cards.base;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import eatyourbeets.interfaces.delegates.FuncT2;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.pcl.special.QuestionMark;
import pinacolada.resources.PGR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PCLCardBuilder
{
    public AbstractCard.CardColor cardColor = AbstractCard.CardColor.COLORLESS;
    public AbstractCard.CardRarity cardRarity = AbstractCard.CardRarity.BASIC;
    public AbstractCard.CardTarget cardTarget = AbstractCard.CardTarget.NONE;
    public AbstractCard.CardType cardType = AbstractCard.CardType.SKILL;
    public ActionT1<PCLCard> constructor;
    public ActionT1<PCLCard> onUpgrade;
    public ActionT3<AbstractPlayer, AbstractMonster, CardUseInfo> onUse;
    public AdvancedTexture portraitForeground;
    public AdvancedTexture portraitImage;
    public ArrayList<AbstractCard.CardTags> tags = new ArrayList<>();
    public ArrayList<AbstractCard.CardTags> upgradeTags;
    public CardSeries series;
    public CardStrings cardStrings;
    public FuncT1<AbstractAttribute, PCLCard> getBlockInfo;
    public FuncT1<AbstractAttribute, PCLCard> getDamageInfo;
    public FuncT1<AbstractAttribute, PCLCard> getSpecialInfo;
    public FuncT2<Boolean, PCLCard, AbstractMonster> canUse;
    public AbstractGameAction.AttackEffect attackEffect = AbstractGameAction.AttackEffect.NONE; //TODO
    public PCLAttackType attackType = PCLAttackType.Normal;
    public PCLCardAffinities affinities = new PCLCardAffinities(null);
    public PCLCardTarget attackTarget = PCLCardTarget.None;
    public String imagePath;
    public TextureAtlas.AtlasRegion fakePortrait;
    public final ArrayList<BaseEffect> effects = new ArrayList<>();
    public boolean canUpgrade = true;
    public boolean isMultiDamage;
    public boolean showTypeText = true;
    public int attributeMultiplier = 1;
    public int block;
    public int blockUpgrade;
    public int cost = -2;
    public int costUpgrade;
    public int damage;
    public int damageUpgrade;
    public int hitCount = 1;
    public int hitCountUpgrade;
    public int magicNumber;
    public int magicNumberUpgrade;
    public int secondaryValue;
    public int secondaryValueUpgrade;
    public String id;

    public PCLCardBuilder(String id)
    {
        this.id = id;
    }

    public PCLCardBuilder(PCLCard card, boolean copyNumbers)
    {
        this(card, card.name, card.rawDescription, copyNumbers);
    }

    public PCLCardBuilder(PCLCard card, String text, boolean copyNumbers)
    {
        this(card, card.name, text, copyNumbers);
    }

    public PCLCardBuilder(PCLCard card, String name, String text, boolean copyNumbers)
    {
        this(card.cardID);

        if (copyNumbers)
        {
            SetNumbers(card.damage, card.block, card.magicNumber, card.secondaryValue, card.hitCount);
            SetUpgrades(card.upgrade_damage, card.upgrade_block, card.upgrade_magicNumber, card.upgrade_secondaryValue, card.upgrade_hitCount);
            SetCost(card.cost, card.upgrade_cost);
            affinities.Initialize(card.affinities);
        }

        SetImage(card.portraitImg, card.portraitForeground);
        SetProperties(card.type, card.color, card.rarity);
        SetText(name, text, text);
        SetSeries(card.series);
    }

    public PCLCardBuilder(PCLCardBuilder original)
    {
        this(original.id);

        SetNumbers(original.damage, original.block, original.magicNumber, original.secondaryValue, original.hitCount);
        SetUpgrades(original.damageUpgrade, original.blockUpgrade, original.magicNumberUpgrade, original.secondaryValueUpgrade, original.hitCountUpgrade);
        SetCost(original.cost, original.costUpgrade);
        affinities.Initialize(original.affinities);
        SetImage(original.portraitImage, original.portraitForeground);
        SetProperties(original.cardType, original.cardColor, original.cardRarity);
        SetAttackType(original.attackType);
        SetCardTarget(original.attackTarget);
        SetText(original.cardStrings.NAME, original.cardStrings.DESCRIPTION, original.cardStrings.UPGRADE_DESCRIPTION, original.cardStrings.EXTENDED_DESCRIPTION);
        SetSeries(original.series);
        SetTags(original.tags);
        SetBaseEffect(true, true, original.effects.toArray(new BaseEffect[]{}));
        SetDamageInfo(original.getDamageInfo);
        SetBlockInfo(original.getBlockInfo);
        SetSpecialInfo(original.getSpecialInfo);
        SetOnUpgrade(original.onUpgrade);
        SetOnUse(original.onUse);
        SetConstructor(original.constructor);
    }

    public PCLCard_Dynamic Build()
    {
        if (cardStrings == null)
        {
            SetText("", "", "");
        }

        if (imagePath == null)
        {
            imagePath = QuestionMark.DATA.ImagePath;
        }

        return new PCLCard_Dynamic(this);
    }

    public PCLCardBuilder SetColor(AbstractCard.CardColor color) {
        this.cardColor = color;
        return this;
    }

    public PCLCardBuilder SetRarity(AbstractCard.CardRarity rarity) {
        this.cardRarity = rarity;
        return this;
    }

    public PCLCardBuilder SetType(AbstractCard.CardType type) {
        this.cardType = type;
        return this;
    }

    public PCLCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardRarity rarity)
    {
        return SetProperties(type, PGR.Enums.Cards.THE_FOOL, rarity);
    }

    public PCLCardBuilder SetProperties(AbstractCard.CardType type, AbstractCard.CardColor color, AbstractCard.CardRarity rarity)
    {
        this.cardType = type;
        this.cardColor = color;
        this.cardRarity = rarity;
        return this;
    }

    public PCLCardBuilder SetID(String id)
    {
        this.id = id;
        return this;
    }

    public PCLCardBuilder SetCost(int baseCost)
    {
        this.cost = baseCost;
        return this;
    }

    public PCLCardBuilder SetCost(int baseCost, int costUpgrade)
    {
        this.cost = baseCost;
        this.costUpgrade = costUpgrade;
        return this;
    }

    public PCLCardBuilder SetCostUpgrade(int costUpgrade)
    {
        this.costUpgrade = costUpgrade;
        return this;
    }

    public PCLCardBuilder SetDamage(int amount)
    {
        this.damage = amount;
        return this;
    }

    public PCLCardBuilder SetDamage(int amount, int upgrade)
    {
        this.damage = amount;
        this.damageUpgrade = upgrade;
        return this;
    }

    public PCLCardBuilder SetBlock(int amount)
    {
        this.block = amount;
        return this;
    }

    public PCLCardBuilder SetBlock(int amount, int upgrade)
    {
        this.block = amount;
        this.blockUpgrade = upgrade;
        return this;
    }

    public PCLCardBuilder SetMagicNumber(int amount)
    {
        this.magicNumber = amount;
        return this;
    }

    public PCLCardBuilder SetMagicNumber(int amount, int upgrade)
    {
        this.magicNumber = amount;
        this.magicNumberUpgrade = upgrade;
        return this;
    }

    public PCLCardBuilder SetSecondaryValue(int amount)
    {
        this.secondaryValue = amount;
        return this;
    }

    public PCLCardBuilder SetSecondaryValue(int amount, int upgrade)
    {
        this.secondaryValue = amount;
        this.secondaryValueUpgrade = upgrade;
        return this;
    }

    public PCLCardBuilder SetHitCount(int amount)
    {
        this.hitCount = amount;
        return this;
    }

    public PCLCardBuilder SetHitCount(int amount, int upgrade)
    {
        this.hitCount = amount;
        this.hitCountUpgrade = upgrade;
        return this;
    }

    public PCLCardBuilder SetDamageUpgrade(int amount)
    {
        this.damageUpgrade = amount;
        return this;
    }

    public PCLCardBuilder SetBlockUpgrade(int amount)
    {
        this.blockUpgrade = amount;
        return this;
    }

    public PCLCardBuilder SetMagicNumberUpgrade(int amount)
    {
        this.magicNumberUpgrade = amount;
        return this;
    }

    public PCLCardBuilder SetSecondaryValueUpgrade(int amount)
    {
        this.secondaryValueUpgrade = amount;
        return this;
    }

    public PCLCardBuilder SetHitCountUpgrade(int amount)
    {
        this.hitCountUpgrade = amount;
        return this;
    }

    public PCLCardBuilder SetNumbers(PCLCard source)
    {
        this.damage = source.baseDamage;
        this.block = source.baseBlock;
        this.magicNumber = source.baseMagicNumber;
        this.secondaryValue = source.baseSecondaryValue;
        this.hitCount = source.hitCount;
        this.damageUpgrade = source.upgrade_damage;
        this.blockUpgrade = source.upgrade_block;
        this.magicNumberUpgrade = source.upgrade_magicNumber;
        this.secondaryValueUpgrade = source.upgrade_secondaryValue;
        this.hitCountUpgrade = source.upgrade_hitCount;

        return this;
    }

    public PCLCardBuilder SetNumbers(int damage, int block, int magicNumber, int secondaryValue, int hitCount)
    {
        this.damage = damage;
        this.block = block;
        this.magicNumber = magicNumber;
        this.secondaryValue = secondaryValue;
        this.hitCount = hitCount;

        return this;
    }

    public PCLCardBuilder SetUpgrades(int damage, int block, int magicNumber, int secondaryValue, int hitCount)
    {
        this.damageUpgrade = damage;
        this.blockUpgrade = block;
        this.magicNumberUpgrade = magicNumber;
        this.secondaryValueUpgrade = secondaryValue;
        this.hitCountUpgrade = hitCount;

        return this;
    }

    public PCLCardBuilder SetTags(AbstractCard card) {
        ArrayList<AbstractCard.CardTags> tags = new ArrayList<>(card.tags);
        if (card.exhaust || card.exhaustOnUseOnce) {
            tags.add(PGR.Enums.CardTags.PCL_EXHAUST);
        }
        if (card.retain) {
            tags.add(PGR.Enums.CardTags.PCL_RETAIN_ONCE);
        }
        if (card.selfRetain) {
            tags.add(PGR.Enums.CardTags.PCL_RETAIN);
        }
        if (card.isEthereal) {
            tags.add(PGR.Enums.CardTags.PCL_ETHEREAL);
        }
        if (card.isInnate) {
            tags.add(PGR.Enums.CardTags.PCL_INNATE);
        }
        if (card.purgeOnUse) {
            tags.add(PGR.Enums.CardTags.PURGE);
        }
        return SetTags(tags);
    }

    public PCLCardBuilder SetTags(List<AbstractCard.CardTags> tags, boolean reset)
    {
        if (reset) {
            this.tags.clear();
        }
        for (AbstractCard.CardTags t : tags)
        {
            if (!this.tags.contains(t))
            {
                this.tags.add(t);
            }
        }

        return this;
    }

    public PCLCardBuilder SetTags(List<AbstractCard.CardTags> tags) {
        return SetTags(tags, false);
    }

    public PCLCardBuilder SetTags(AbstractCard.CardTags... tags)
    {
        return SetTags(Arrays.asList(tags), false);
    }

    public PCLCardBuilder RemoveTags(List<AbstractCard.CardTags> tags)
    {
        for (AbstractCard.CardTags t : tags)
        {
            this.tags.remove(t);
        }

        return this;
    }

    public PCLCardBuilder RemoveTags(AbstractCard.CardTags... tags)
    {
        return RemoveTags(Arrays.asList(tags));
    }

    public PCLCardBuilder SetAttackEffect(AbstractGameAction.AttackEffect attackEffect)
    {
        this.attackEffect = attackEffect;

        return this;
    }

    public PCLCardBuilder SetAttackType(PCLAttackType attackType)
    {
        this.attackType = attackType;

        return this;
    }

    public PCLCardBuilder SetAffinities(PCLCardAffinities affinities)
    {
        this.affinities.Initialize(affinities);

        return this;
    }

    public PCLCardBuilder SetAffinities(Integer[] base, Integer[] upgrade, Integer[] scaling, Integer[] requirement)
    {
        this.affinities.Initialize(base, upgrade, scaling, requirement);

        return this;
    }

    public PCLCardBuilder SetAffinity(PCLAffinity affinity, int level)
    {
        this.affinities.Set(affinity, level);

        return this;
    }

    public PCLCardBuilder SetAffinity(PCLAffinity affinity, int level, int upgrade, int scaling)
    {
        this.affinities.Initialize(affinity, level, upgrade, scaling);

        return this;
    }

    public PCLCardBuilder SetAffinityScaling(PCLAffinity affinity, int level)
    {
        this.affinities.SetScaling(affinity, level);

        return this;
    }

    public PCLCardBuilder SetAffinityRequirement(PCLAffinity affinity, int level)
    {
        this.affinities.SetRequirement(affinity, level);

        return this;
    }

    public PCLCardBuilder SetDamageInfo(FuncT1<AbstractAttribute, PCLCard> getDamageInfo)
    {
        this.getDamageInfo = getDamageInfo;

        return this;
    }

    public PCLCardBuilder SetBlockInfo(FuncT1<AbstractAttribute, PCLCard> getBlockInfo)
    {
        this.getBlockInfo = getBlockInfo;

        return this;
    }

    public PCLCardBuilder SetSpecialInfo(FuncT1<AbstractAttribute, PCLCard> getSpecialInfo)
    {
        this.getSpecialInfo = getSpecialInfo;

        return this;
    }

    public PCLCardBuilder SetCardTarget(PCLCardTarget cardTarget) {
        this.attackTarget = cardTarget;
        this.isMultiDamage = (attackTarget == PCLCardTarget.AoE);
        this.cardTarget = attackTarget.cardTarget;

        return this;
    }

    public PCLCardBuilder SetImage(AdvancedTexture portraitImage, AdvancedTexture portraitForeground)
    {
        this.portraitImage = portraitImage;
        this.portraitForeground = portraitForeground;

        return this;
    }

    public PCLCardBuilder SetImagePath(String imagePath)
    {
        this.imagePath = imagePath;

        return this;
    }

    public PCLCardBuilder SetPortrait(TextureAtlas.AtlasRegion portrait)
    {
        this.fakePortrait = portrait;

        return this;
    }

    public PCLCardBuilder SetDescription(String description) {
        if (this.cardStrings == null) {
            this.cardStrings = new CardStrings();
        }
        this.cardStrings.DESCRIPTION = description;

        return this;
    }

    public PCLCardBuilder SetName(String name) {
        if (this.cardStrings == null) {
            this.cardStrings = new CardStrings();
        }
        this.cardStrings.NAME = name;

        return this;
    }

    public PCLCardBuilder SetText(CardStrings cardStrings)
    {
        return SetText(cardStrings.NAME, cardStrings.DESCRIPTION, cardStrings.UPGRADE_DESCRIPTION);
    }

    public PCLCardBuilder SetText(String name, String description, String upgradeDescription)
    {
        return SetText(name, description, upgradeDescription != null ? upgradeDescription : description, new String[0]);
    }

    public PCLCardBuilder SetText(String name, String description, String upgradeDescription, String[] extendedDescription)
    {
        if (this.cardStrings == null) {
            this.cardStrings = new CardStrings();
        }
        this.cardStrings.NAME = name;
        this.cardStrings.DESCRIPTION = description;
        this.cardStrings.UPGRADE_DESCRIPTION = upgradeDescription;
        this.cardStrings.EXTENDED_DESCRIPTION = extendedDescription;

        return this;
    }

    public PCLCardBuilder SetConstructor(ActionT1<PCLCard> constructor)
    {
        this.constructor = constructor;

        return this;
    }

    public PCLCardBuilder SetOnUpgrade(ActionT1<PCLCard> onUpgrade)
    {
        this.onUpgrade = onUpgrade;

        return this;
    }

    public PCLCardBuilder SetOnUse(ActionT3<AbstractPlayer, AbstractMonster, CardUseInfo> onUseAction)
    {
        this.onUse = onUseAction;

        return this;
    }

    public PCLCardBuilder SetSeries(CardSeries series)
    {
        this.series = series;

        return this;
    }

    public PCLCardBuilder CanUpgrade(boolean canUpgrade)
    {
        this.canUpgrade = canUpgrade;

        return this;
    }

    public PCLCardBuilder SetBaseEffect(BaseEffect... effect) {
        return SetBaseEffect(false, false, effect);
    }

    public PCLCardBuilder SetBaseEffect(boolean makeCopy, boolean clear, BaseEffect... effect) {
        if (clear) {
            effects.clear();
        }
        for (BaseEffect be : effect) {
            SetBaseEffect(be, makeCopy);
        }
        return this;
    }

    public PCLCardBuilder SetBaseEffect(BaseEffect effect) {
        return SetBaseEffect(effect,  false);
    }

    public PCLCardBuilder SetBaseEffect(BaseEffect effect, boolean makeCopy)
    {
        if (makeCopy) {
            effect = effect.MakeCopy();
        }
        effects.add(effect);

        return this;
    }

    public PCLCardBuilder RemoveBaseEffect(BaseEffect effect)
    {
        effects.remove(effect);
        return this;
    }

    public PCLCardBuilder CanUse(FuncT2<Boolean, PCLCard, AbstractMonster> canUse) {
        this.canUse = canUse;

        return this;
    }

    public PCLCardBuilder ShowTypeText(boolean showTypeText)
    {
        this.showTypeText = showTypeText;

        return this;
    }
}