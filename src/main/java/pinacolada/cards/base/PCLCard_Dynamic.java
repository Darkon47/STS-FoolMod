package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.interfaces.delegates.FuncT1;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.baseeffects.BaseCondition;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.base.baseeffects.effects.BaseEffect_DealCardDamage;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class PCLCard_Dynamic extends PCLCard
{
    protected final PCLCardBuilder builder;

    public boolean canUpgrade;
    public boolean canSelect;
    public int attributeMultiplier;
    public final ActionT1<PCLCard> constructor;
    public final ActionT1<PCLCard> onUpgrade;
    public final ActionT3<AbstractPlayer, AbstractMonster, CardUseInfo> onUse;
    public final FuncT1<AbstractAttribute, PCLCard> getSpecialInfo;
    public final FuncT1<AbstractAttribute, PCLCard> getDamageInfo;
    public final FuncT1<AbstractAttribute, PCLCard> getBlockInfo;
    protected ArrayList<CardTags> upgradeTags;

    public PCLCard_Dynamic(PCLCardBuilder builder)
    {
        super(new PCLCardData(PCLCard_Dynamic.class, builder.id, builder.cardStrings), builder.id, builder.imagePath,
            builder.cost, builder.cardType, builder.cardColor, builder.cardRarity, builder.cardTarget);

        Initialize(builder.damage, builder.block, builder.magicNumber, builder.secondaryValue);
        SetUpgrade(builder.damageUpgrade, builder.blockUpgrade, builder.magicNumberUpgrade, builder.secondaryValueUpgrade);
        SetCostUpgrade(builder.costUpgrade);
        SetHitCount(builder.hitCount,builder.hitCountUpgrade);

        this.attributeMultiplier = builder.attributeMultiplier;
        this.affinities.Initialize(builder.affinities);
        this.attackTarget = builder.attackTarget;
        this.attackType = builder.attackType;
        this.builder = builder;
        this.onUse = builder.onUse;
        this.onUpgrade = builder.onUpgrade;
        this.constructor = builder.constructor;
        this.isMultiDamage = builder.isMultiDamage;
        this.cropPortrait = false;
        this.canUpgrade = builder.canUpgrade;
        this.showTypeText = builder.showTypeText;

        if (builder.portraitImage != null)
        {
            this.portraitImg = builder.portraitImage;
        }
        if (builder.portraitForeground != null)
        {
            this.portraitForeground = builder.portraitForeground;
        }
        if (builder.fakePortrait != null) {
            this.fakePortrait = builder.fakePortrait;
        }

        this.getSpecialInfo = builder.getSpecialInfo;
        this.getDamageInfo = builder.getDamageInfo;
        this.getBlockInfo = builder.getBlockInfo;

        if (constructor != null)
        {
            constructor.Invoke(this);
        }

        for (CardTags tag : builder.tags) {
            PCLGameUtilities.ModifyCardTag(this, tag, true);
        }
        this.upgradeTags = builder.upgradeTags;

        SetSeries(builder.series);

        // TODO Temp HP condition
        boolean hasAttack = false;
        for (BaseEffect effect : builder.effects) {
            if (effect == null) {
                continue;
            }
            if (effect instanceof BaseCondition) {
                if (((BaseCondition) effect).HasCreate()) {
                    onCreateEffect = effect;
                    continue;
                }
                else if (((BaseCondition) effect).HasDiscard()) {
                    onDiscardEffect = effect;
                    continue;
                }
                else if (((BaseCondition) effect).HasDraw()) {
                    onDrawEffect = effect;
                    continue;
                }
                else if (((BaseCondition) effect).HasExhaust()) {
                    onExhaustEffect = effect;
                    continue;
                }
                else if (((BaseCondition) effect).HasPurge()) {
                    onPurgeEffect = effect;
                    continue;
                }
            }
            if (effect instanceof BaseEffect_DealCardDamage) {
                hasAttack = true;
            }
            onUseEffects.add(effect);
        }

        // Automatically create Attack actions for Attacks that specify damage but that do not already have attack actions
        if (builder.damage > 0 && builder.cardType == CardType.ATTACK && !hasAttack) {
            onUseEffects.add(new BaseEffect_DealCardDamage(this, builder.attackEffect));
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (getDamageInfo != null)
        {
            return getDamageInfo.Invoke(this);
        }

        AbstractAttribute info = super.GetDamageInfo();
        if (info != null && attributeMultiplier > 1)
        {
            info.AddMultiplier(attributeMultiplier);
        }

        return info;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        if (getBlockInfo != null)
        {
            return getBlockInfo.Invoke(this);
        }

        AbstractAttribute info = super.GetBlockInfo();
        if (info != null && attributeMultiplier > 1)
        {
            info.AddMultiplier(attributeMultiplier);
        }

        return info;
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        if (getSpecialInfo != null)
        {
            return getSpecialInfo.Invoke(this);
        }

        return super.GetSpecialInfo();
    }

    @Override
    protected void OnUpgrade()
    {
        if (onUpgrade != null)
        {
            onUpgrade.Invoke(this);
        }
        if (upgradeTags != null) {
            tags.clear();
            for (CardTags tag : upgradeTags) {
                PCLGameUtilities.ModifyCardTag(this, tag, true);
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (onUse != null)
        {
            onUse.Invoke(p, m, info);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return canUpgrade && super.canUpgrade();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return new PCLCard_Dynamic(builder);
    }
}