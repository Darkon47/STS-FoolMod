package eatyourbeets.cards.base;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.ui.animator.cardReward.AnimatorCardBadgeLegend;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RenderHelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EYBCard extends EYBCardBase
{
    public enum AttackType
    {
        Normal(false, false),
        Elemental(true, true),
        Piercing(true, true),
        Ranged(false, true);

        public final boolean bypassThorns;
        public final boolean bypassBlock;

        AttackType(boolean bypassBlock, boolean bypassThorns)
        {
            this.bypassThorns = bypassThorns;
            this.bypassBlock = bypassBlock;
        }
    }

    protected abstract ColoredString GetBottomText();

    protected static final Map<String, EYBCardData> staticCardData = new HashMap<>();

    public AttackType attackType = AttackType.Normal;
    public final List<TooltipInfo> customTooltips = new ArrayList<>();
    public boolean hovered = false;
    public boolean hoveredInHand = false;

    protected final EYBCardText cardText;
    protected final EYBCardData cardData;
    protected boolean isMultiUpgrade;
    protected int upgrade_damage;
    protected int upgrade_magicNumber;
    protected int upgrade_secondaryValue;
    protected int upgrade_block;
    protected int upgrade_cost;

    public float forceScaling = 0;
    public float intellectScaling = 0;
    public float agilityScaling = 0;

    public static EYBCardData GetCardData(String cardID)
    {
        return staticCardData.get(cardID);
    }

    public static String RegisterCard(Class<? extends EYBCard> type, String cardID)
    {
        staticCardData.put(cardID, new EYBCardData(type, GR.GetCardStrings(cardID)));

        return cardID;
    }

    protected EYBCard(EYBCardData cardData, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(id, cardData.strings.NAME, imagePath, cost, "", type, color, rarity, target);

        this.cardData = cardData;
        this.cardText = new EYBAdvancedCardText(this, cardData.strings);
        this.cardText.ForceRefresh();
    }

    @Override
    public AbstractCard makeCopy()
    {
        return cardData.CreateNewInstance();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        EYBCard copy = (EYBCard) super.makeStatEquivalentCopy();

        copy.magicNumber = this.magicNumber;
        copy.isMagicNumberModified = this.isMagicNumberModified;

        copy.secondaryValue = this.secondaryValue;
        copy.baseSecondaryValue = this.baseSecondaryValue;
        copy.isSecondaryValueModified = this.isSecondaryValueModified;

        return copy;
    }

    @Override
    public void hover()
    {
        super.hover();

        hovered = true;
    }

    @Override
    public void unhover()
    {
        super.unhover();

        hovered = false;
    }

    @Override
    public void render(SpriteBatch sb)
    {
        if (!Settings.hideCards || transparency <= 0.1f)
        {
            if (!isPreview && AnimatorCardBadgeLegend.showUpgrades && canUpgrade() && !CardCrawlGame.isPopupOpen && SingleCardViewPopup.isViewingUpgrade)
            {
                EYBCard upgrade = cardData.tempCard;

                if (upgrade == null || upgrade.uuid != this.uuid || (upgrade.timesUpgraded != (timesUpgraded + 1)))
                {
                    upgrade = cardData.tempCard = (EYBCard) this.makeSameInstanceOf();
                    upgrade.isPreview = true;
                    upgrade.upgrade();
                    upgrade.displayUpgrades();
                }

                upgrade.current_x = this.current_x;
                upgrade.current_y = this.current_y;
                upgrade.drawScale = this.drawScale;
                upgrade.render(sb);
            }
            else
            {
                super.render(sb);
                RenderHeader(sb);
            }
        }
    }

    @Override
    public void renderCardPreview(SpriteBatch sb)
    {
        if (isPopup)
        {
            cardsToPreview.current_x = (float) Settings.WIDTH * 0.2f - 10.0F * Settings.scale;
            cardsToPreview.current_y = (float) Settings.HEIGHT * 0.25f;
            cardsToPreview.drawScale = 1f;
            cardsToPreview.render(sb);
        }
        else
        {
            super.renderCardPreview(sb);
        }
    }

    @Override
    public void renderInLibrary(SpriteBatch sb)
    {
        if (this.isOnScreen())
        {
            if (SingleCardViewPopup.isViewingUpgrade && this.isSeen && !this.isLocked)
            {
                super.renderInLibrary(sb);
            }
            else
            {
                super.renderInLibrary(sb);
                RenderHeader(sb);
            }
        }
    }

    @Override
    public void initializeDescription()
    {
        if (cardText != null)
        {
            this.cardText.InitializeDescription();
        }
    }

    @Override
    public void initializeDescriptionCN()
    {
        if (cardText != null)
        {
            this.cardText.InitializeDescription();
        }
    }

    @SpireOverride
    public void renderDescription(SpriteBatch sb)
    {
        this.cardText.RenderDescription(sb);
    }

    @Override
    public void renderCardTip(SpriteBatch sb)
    {
        this.cardText.RenderTooltips(sb);
    }

    @Override
    public void triggerWhenCopied()
    {
        // this is only used by ShowCardAndAddToHandEffect
        super.triggerWhenCopied();
        triggerWhenDrawn();
    }

    protected ColoredString GetHeaderText()
    {
        return null;
    }

    protected void RenderHeader(SpriteBatch sb)
    {
        ColoredString string = GetHeaderText();
        if (string == null || this.isFlipped || this.isLocked)
        {
            return;
        }

        float scaleMulti = 0.8f;
        int length = string.text.length();
        if (length > 20)
        {
            scaleMulti -= 0.02f * (length - 20);
            if (scaleMulti < 0.5f)
            {
                scaleMulti = 0.5f;
            }
        }

        BitmapFont font = isPopup ? FontHelper.SCP_cardTitleFont_small : FontHelper.cardTitleFont_small;

        font.getData().setScale(scaleMulti * this.drawScale * (isPopup ? 0.5f : 1f));

        RenderHelpers.WriteOnCard(sb, this, font, string.text, 0, AbstractCard.RAW_H * 0.48f, string.color, true);

        font.getData().setScale(1);
    }

    protected void AddExtendedDescription()
    {
        AddExtendedDescription(0, 1);
    }

    protected void AddExtendedDescription(Object param)
    {
        String[] info = this.cardData.strings.EXTENDED_DESCRIPTION;
        AddTooltip(new TooltipInfo(info[0], info[1] + param + info[2]));
    }

    protected void AddExtendedDescription(int headerIndex, int contentIndex)
    {
        String[] info = this.cardData.strings.EXTENDED_DESCRIPTION;
        if (info != null && info.length >= 2 && info[headerIndex].length() > 0)
        {
            AddTooltip(new TooltipInfo(info[headerIndex], info[contentIndex]));
        }
    }

    protected void AddTooltip(TooltipInfo tooltip)
    {
        customTooltips.add(tooltip);
    }

    public boolean IsAoE()
    {
        return isMultiDamage;
    }

    public AbstractAttribute GetDamageInfo()
    {
        if (baseDamage >= 0)
        {
            return DamageAttribute.Instance.SetCard(this);
        }
        else
        {
            return null;
        }
    }

    public AbstractAttribute GetBlockInfo()
    {
        if (baseBlock >= 0)
        {
            return BlockAttribute.Instance.SetCard(this);
        }
        else
        {
            return null;
        }
    }

    public AbstractAttribute GetSpecialInfo()
    {
        return null;
    }

    public void SetAttackType(AttackType attackType)
    {
        this.attackType = attackType;
    }

    public void SetMultiDamage(boolean value)
    {
        this.isMultiDamage = value;
    }

    public void SetRetainOnce(boolean value)
    {
        this.retain = value;
    }

    public void SetRetain(boolean value)
    {
        this.selfRetain = value;
    }

    public void SetInnate(boolean value)
    {
        this.isInnate = value;
    }

    public void SetExhaust(boolean value)
    {
        this.exhaust = value;
    }

    public void SetEthereal(boolean value)
    {
        this.isEthereal = value;
    }

    public void SetEvokeOrbCount(int count)
    {
        this.showEvokeValue = count > 0;
        this.showEvokeOrbCount = count;
    }

    public void SetLoyal(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.LOYAL))
            {
                tags.add(GR.Enums.CardTags.LOYAL);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.LOYAL);
        }
    }

    public void SetPiercing(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.PIERCING))
            {
                tags.add(GR.Enums.CardTags.PIERCING);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.PIERCING);
        }
    }

    public void SetHealing(boolean value)
    {
        if (value)
        {
            if (!tags.contains(CardTags.HEALING))
            {
                tags.add(CardTags.HEALING);
            }
        }
        else
        {
            tags.remove(CardTags.HEALING);
        }
    }

    public void SetPurge(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.PURGE))
            {
                tags.add(GR.Enums.CardTags.PURGE);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.PURGE);
        }
    }

    public void SetUnique(boolean value, boolean multiUpgrade)
    {
        isMultiUpgrade = multiUpgrade;

        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.UNIQUE))
            {
                tags.add(GR.Enums.CardTags.UNIQUE);

                if (multiUpgrade)
                {
                    EYBCardTooltip unique = GR.GetTooltip("~unique");
                    AddTooltip(new TooltipInfo(unique.title, unique.description));
                }
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.UNIQUE);
        }
    }

    protected boolean TryUpgrade()
    {
        return TryUpgrade(true);
    }

    protected boolean TryUpgrade(boolean updateDescription)
    {
        if (this.canUpgrade())
        {
            this.timesUpgraded += 1;
            this.upgraded = true;

            if (isMultiUpgrade)
            {
                this.name = cardData.strings.NAME + "+" + this.timesUpgraded;
            }
            else
            {
                this.name = cardData.strings.NAME + "+";
            }

            initializeTitle();

            if (updateDescription)
            {
                cardText.ForceRefresh();
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean canUpgrade()
    {
        return !upgraded || isMultiUpgrade;
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            if (upgrade_damage != 0)
            {
                if (baseDamage < 0)
                {
                    baseDamage = 0;
                }

                upgradeDamage(upgrade_damage);
            }

            if (upgrade_block != 0)
            {
                if (baseBlock < 0)
                {
                    baseBlock = 0;
                }

                upgradeBlock(upgrade_block);
            }

            if (upgrade_secondaryValue != 0)
            {
                upgradeSecondaryValue(upgrade_secondaryValue);
            }

            if (upgrade_magicNumber != 0)
            {
                upgradeMagicNumber(upgrade_magicNumber);
            }

            if (upgrade_cost != 0)
            {
                int previousCost = cost;
                int previousCostForTurn = costForTurn;

                this.cost = Math.max(0, previousCost + upgrade_cost);
                this.costForTurn = Math.max(0, previousCostForTurn + upgrade_cost);
                this.upgradedCost = true;
            }

            OnUpgrade();
        }
    }

    @Override
    public void displayUpgrades()
    {
        super.displayUpgrades();

        if (this.upgradedSecondaryValue)
        {
            this.secondaryValue = this.baseSecondaryValue;
            this.isSecondaryValueModified = true;
        }
    }

    protected void Initialize(int damage, int block)
    {
        Initialize(damage, block, -1, 0);
    }

    protected void Initialize(int damage, int block, int magicNumber)
    {
        Initialize(damage, block, magicNumber, 0);
    }

    protected void Initialize(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.baseDamage = this.damage = damage > 0 ? damage : -1;
        this.baseBlock = this.block = block > 0 ? block : -1;
        this.baseMagicNumber = this.magicNumber = magicNumber;
        this.baseSecondaryValue = this.secondaryValue = secondaryValue;
    }

    protected void SetScaling(float intellect, float agility, float force)
    {
        this.intellectScaling = intellect;
        this.agilityScaling = agility;
        this.forceScaling = force;
    }

    protected void SetUpgrade(int damage, int block)
    {
        SetUpgrade(damage, block, 0, 0);
    }

    protected void SetUpgrade(int damage, int block, int magicNumber)
    {
        SetUpgrade(damage, block, magicNumber, 0);
    }

    protected void SetUpgrade(int damage, int block, int magicNumber, int secondaryValue)
    {
        this.upgrade_damage = damage;
        this.upgrade_block = block;
        this.upgrade_magicNumber = magicNumber;
        this.upgrade_secondaryValue = secondaryValue;
    }

    protected void SetCostUpgrade(int value)
    {
        this.upgrade_cost = value;
    }

    protected void OnUpgrade()
    {

    }

    @Override
    protected final void applyPowersToBlock()
    {
        throw new RuntimeException("This method must not be called");
    }

    @Override
    public final void applyPowers()
    {
        calculateCardDamage(null);
    }

    @Override
    public void calculateDamageDisplay(AbstractMonster mo)
    {
        calculateCardDamage(mo);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo)
    {
        if (isMultiDamage)
        {
            ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
            multiDamage = new int[m.size()];

            int best = -999;
            for (int i = 0; i < multiDamage.length; i++)
            {
                if (damage > best)
                {
                    best = damage;
                }

                Refresh(m.get(i));
                multiDamage[i] = damage;
            }

            if (best > 0)
            {
                UpdateDamage(best);
            }
        }
        else
        {
            Refresh(mo);
        }
    }

    protected void Refresh(AbstractMonster enemy)
    {
        boolean applyEnemyPowers = (enemy != null && !GameUtilities.IsDeadOrEscaped(enemy));
        float tempBlock = GetInitialBlock();
        float tempDamage = GetInitialDamage();

        for (AbstractRelic r : player.relics)
        {
            tempDamage = r.atDamageModify(tempDamage, this);
        }

        for (AbstractPower p : player.powers)
        {
            tempBlock = p.modifyBlock(tempBlock, this);
            tempDamage = p.atDamageGive(tempDamage, damageTypeForTurn, this);
        }

        tempBlock = ModifyBlock(enemy, tempBlock);
        tempDamage = ModifyDamage(enemy, tempDamage);

        if (applyEnemyPowers)
        {
            for (AbstractPower p : enemy.powers)
            {
                tempDamage = p.atDamageReceive(tempDamage, damageTypeForTurn, this);
            }
        }

        tempDamage = player.stance.atDamageGive(tempDamage, this.damageTypeForTurn, this);

        for (AbstractPower p : player.powers)
        {
            tempDamage = p.atDamageFinalGive(tempDamage, damageTypeForTurn, this);
        }

        if (applyEnemyPowers)
        {
            for (AbstractPower p : enemy.powers)
            {
                tempDamage = p.atDamageFinalReceive(tempDamage, damageTypeForTurn, this);
            }
        }

        UpdateBlock(tempBlock);
        UpdateDamage(tempDamage);

        JavaUtilities.Log(this, cardID + ", Updating Damage: " + tempDamage);
    }

    protected void UpdateBlock(float amount)
    {
        block = MathUtils.floor(amount);
        if (block < 0)
        {
            block = 0;
        }
        this.isBlockModified = (baseBlock != block);
    }

    protected void UpdateDamage(float amount)
    {
        damage = MathUtils.floor(amount);
        if (damage < 0)
        {
            damage = 0;
        }
        this.isDamageModified = (baseDamage != damage);
    }

    protected float GetInitialBlock()
    {
        return baseBlock;
    }

    protected float GetInitialDamage()
    {
        return baseDamage;
    }

    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return amount;
    }

    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return amount;
    }
}
