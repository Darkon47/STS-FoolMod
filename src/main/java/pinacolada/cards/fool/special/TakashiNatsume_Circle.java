package pinacolada.cards.fool.special;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.colorless.TakashiNatsume;
import pinacolada.cards.fool.series.GenshinImpact.AyakaKamisato;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.FoolPower;
import pinacolada.powers.common.DeenergizedPower;
import pinacolada.powers.fool.InvertPower;
import pinacolada.powers.temporary.TemporaryThousandCutsPower;
import pinacolada.resources.PGR;
import pinacolada.resources.pcl.PCLStrings;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class TakashiNatsume_Circle extends FoolCard
{
    public enum Form
    {
        None,
        Curse_Delusion,
        Curse_Depression,
        Curse_Eclipse,
        Curse_GriefSeed,
        Curse_Greed,
        Curse_JunTormented,
        Curse_Nutcracker,
        Curse_SearingBurn,
        Curse_Slumber,
        Decay,
        Doubt,
        Necronomicurse,
        Normality,
        Pain,
        Parasite,
        Regret,
        Shame,
        TimeParadox
    }

    private static final PCLStrings.Actions ACTIONS = PGR.PCL.Strings.Actions;
    private static final int DAMAGE_DECAY = 2;
    private static final int HEAL_NUTCRACKER = 5;
    private static final int NORMALITY_HITS = 3;
    public static final PCLCardData DATA = Register(TakashiNatsume_Circle.class).SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.NatsumeYuujinchou);
    private TakashiNatsume_Circle.Form currentForm = Form.None;

    public TakashiNatsume_Circle() {
        this(Form.None);
    }

    public TakashiNatsume_Circle(Form form)
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0, 0, 2, 2);

        SetAffinity_Dark(1);

        SetPurge(true);
        SetHealing(true);

        ChangeForm(form);
    }

    public void ChangeForm(TakashiNatsume_Circle.Form form) {
        currentForm = form;
        switch (form) {
            case Curse_Delusion:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[3], true);
                break;
            case Curse_Depression:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.Draw(secondaryValue, true), true);
                break;
            case Curse_Eclipse:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " +  ACTIONS.Play(AyakaKamisato.DATA.Strings.NAME, true), true);
                break;
            case Curse_Greed:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.Motivate(secondaryValue, true), true);
                break;
            case Curse_GriefSeed:
            case Decay:
                baseDamage = DAMAGE_DECAY * secondaryValue;
                SetAttackType(PCLAttackType.Dark);
                SetAttackTarget(PCLCardTarget.AoE);
                this.type = CardType.ATTACK;
                break;
            case Curse_Nutcracker:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.HealHP(HEAL_NUTCRACKER + magicNumber, false), true);
                break;
            case Curse_JunTormented:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, PGR.Tooltips.Weak, true) + " NL  NL " + ACTIONS.ApplyToALL(magicNumber, PGR.Tooltips.Vulnerable, true), true);
                break;
            case Curse_Slumber:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[5],secondaryValue), true);
                break;
            case Curse_SearingBurn:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, PGR.Tooltips.Burning, true), true);
                break;
            case Doubt:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, PGR.Tooltips.Weak, true), true);
                break;
            case Necronomicurse:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " " + ACTIONS.NextTurnDraw(secondaryValue, true) + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[4], true);
                break;
            case Normality:
                this.cost = 1;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[0],NORMALITY_HITS), true);
                break;
            case Pain:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + PCLJUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[2],secondaryValue), true);
                break;
            case Regret:
                baseDamage = magicNumber;
                SetAttackType(PCLAttackType.Dark);
                SetAttackTarget(PCLCardTarget.Random);
                this.type = CardType.ATTACK;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[1], true);
                break;
            case Shame:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, PGR.Tooltips.Frail, true), true);
                break;
            default:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.NextTurnDraw(secondaryValue, true), true);
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));

        if (EnergyPanel.totalCount < this.costForTurn) {
            PCLActions.Bottom.StackPower(new DeenergizedPower(player, this.costForTurn - EnergyPanel.totalCount));
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int[] damageMatrix;
        switch(currentForm) {
            case Curse_Delusion:
                PCLActions.Bottom.ModifyTag(player.drawPile, 999, AUTOPLAY, false);
                break;
            case Curse_Depression:
                PCLActions.Bottom.Draw(secondaryValue);
                break;
            case Curse_Eclipse:
                AbstractCard c = GetCardForEffect();
                if (upgraded) {
                    c.upgrade();
                }
                c.applyPowers();
                if (PCLGameUtilities.IsPlayerTurn(true)) {
                    PCLActions.Bottom.PlayCopy(c, null);
                }
                else {
                    c.use(player, null);
                }
                break;
            case Curse_JunTormented:
                PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
                PCLActions.Bottom.ApplyFrail(TargetHelper.Enemies(), magicNumber);
                break;
            case Curse_Greed:
                PCLActions.Bottom.Motivate(secondaryValue);
                break;
            case Curse_Nutcracker:
                PCLActions.Bottom.Heal(HEAL_NUTCRACKER + magicNumber);
                break;
            case Decay:
            case Curse_GriefSeed:
                damageMatrix = DamageInfo.createDamageMatrix(damage, true);
                PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
                break;
            case Curse_SearingBurn:
                PCLActions.Bottom.ApplyBurning(TargetHelper.Enemies(), secondaryValue);
                break;
            case Curse_Slumber:
                PCLActions.Bottom.StackPower(player, new InvertPower(player, secondaryValue));
                break;
            case Doubt:
                PCLActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
                break;
            case Necronomicurse:
                PCLActions.Bottom.DrawNextTurn(secondaryValue);
                if (CombatStats.TryActivateLimited(cardID)) {
                    PCLActions.Bottom.SelectFromPile(name,999,player.exhaustPile).SetFilter(card -> card.cardID.equals(TakashiNatsume.DATA.ID)).AddCallback(
                            cards -> {
                                for (AbstractCard card : cards) {
                                    PCLActions.Bottom.MoveCard(card,player.drawPile).AddCallback(cCard -> {
                                        PCLActions.Bottom.Motivate(card, 1);
                                        ((TakashiNatsume) card).SetAfterlife(true);
                                    });
                                }
                            }
                    );
                }
                break;
            case Normality:
                PCLActions.Bottom.StackPower(player, new TakashiNatsumeCirclePower(player, NORMALITY_HITS));
                break;
            case Pain:
                PCLActions.Bottom.StackPower(player, new TemporaryThousandCutsPower(player, secondaryValue));
                break;
            case Regret:
                for (int i = 0; i < player.hand.size(); i++) {
                    PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.FIRE);
                }
                break;
            case Shame:
                PCLActions.Bottom.ApplyFrail(TargetHelper.Enemies(), secondaryValue);
                break;
            default:
                PCLActions.Bottom.DrawNextTurn(secondaryValue);
                break;
        }
    }

    // TODO add more card replacements here
    protected AbstractCard GetCardForEffect() {
        switch(currentForm) {
            case Curse_Eclipse:
                return new AyakaKamisato();
            default:
                return null;
        }
    }

    public static class TakashiNatsumeCirclePower extends FoolPower
    {
        public TakashiNatsumeCirclePower(AbstractPlayer owner, int amount)
        {
            super(owner, TakashiNatsume_Circle.DATA);

            this.amount = amount;
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (this.amount > 0 && info.owner instanceof AbstractMonster && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS)
            {
                PCLActions.Top.ApplyPower(this.owner, new StunMonsterPower((AbstractMonster) info.owner, 1));
                this.amount -= 1;
                this.flash();
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void atEndOfRound() {
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}